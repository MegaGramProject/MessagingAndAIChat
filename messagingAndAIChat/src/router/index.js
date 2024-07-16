import { createRouter, createWebHistory } from 'vue-router';
import App from '../App.vue';
import NotFound from '../NotFound.vue';
import PublicConvo from '../PublicConvo.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/aiChat/:username',
      name: 'app',
      component: App
    },
    {
      path: '/publiclySharedAIConvo/:convoid',
      name: 'publicconvo',
      component: PublicConvo
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'notfound',
      component: NotFound
    }
  ]
})

export default router
