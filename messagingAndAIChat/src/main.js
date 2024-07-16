import './assets/main.css';

import { createPinia } from 'pinia';
import { createApp } from 'vue';

import App from './App.vue';
import PublicConvo from './PublicConvo.vue';
import NotFound from './NotFound.vue';
import router from './router/index';


const currentURL = window.location.href;

if(currentURL.startsWith("http://localhost:8007/aiChat/")) {
    const app = createApp(App)
    app.use(createPinia())
    app.use(router)
    app.mount('#main')
}

else if(currentURL.startsWith("http://localhost:8007/publiclySharedAIConvo/")) {
    const pc = createApp(PublicConvo);
    pc.use(createPinia());
    pc.use(router);
    pc.mount('#main');
}

else {
    const nf = createApp(NotFound);
    nf.use(createPinia());
    nf.use(router);
    nf.mount('#main');
}