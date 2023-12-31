package me.chang.gpms.event;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.chang.gpms.pojo.Report;
import me.chang.gpms.service.ReportService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import me.chang.gpms.pojo.Event;
import me.chang.gpms.pojo.Message;
import me.chang.gpms.service.MessageService;
import me.chang.gpms.util.constant.BbSystemUser;
import me.chang.gpms.util.constant.IBbKafkaTopicConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//import me.chang.gpms.service.ElasticsearchService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件消费者
 */
@Slf4j
@Component
public class EventConsumer implements IBbKafkaTopicConst {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ReportService reportService;

//    @Autowired
//    private ElasticsearchService elasticsearchService;

    /**
     * 消费评论、点赞、关注事件
     * @param record
     */
    @KafkaListener(topics = {TOPIC_COMMNET, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleMessage(ConsumerRecord record) {

        if (record == null || record.value() == null) {
            log.error("消息的内容为空");
            return ;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误");
            return ;
        }

        // 发送系统通知
        Message message = new Message();
        message.setFromId(BbSystemUser.SYSTEM_USER_ID.value());
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());
        if (!event.getData().isEmpty()) { // 存储 Event 中的 Data
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(JSONObject.toJSONString(content));

        messageService.addMessage(message);

    }

    /**
     * 消费发帖事件
     */
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空");
            return ;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误");
            return ;
        }
        Report post = reportService.findReportById(event.getEntityId());
//        elasticsearchService.saveReport(post);

    }

    /**
     * 消费删帖事件
     */
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空");
            return ;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误");
            return ;
        }

//        elasticsearchService.deleteReport(event.getEntityId());

    }

}
