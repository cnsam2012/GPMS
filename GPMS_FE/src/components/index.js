import Vue from 'vue'

import d2Container from './d2-container'

// 注意 有些组件使用异步加载会有影响
Vue.component('d2-container', d2Container)
Vue.component('d2-icon', () => import('./d2-icon'))
Vue.component('d2-icon-svg', () => import('./d2-icon-svg/index.vue'))
Vue.component('random-motto', () => import('./random-motto/index.vue'))
// Vue.component('editor-md', () => import('./editor-md/index.vue'))
