import { createRouter, createWebHistory } from 'vue-router';
import App from '../App.vue';
import NotFound from '../NotFound.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/aiChat/:username',
      name: 'app',
      component: App
    },
  ]
})

export default router
