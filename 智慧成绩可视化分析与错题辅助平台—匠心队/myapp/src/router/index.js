import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/login'
import Rank from '@/components/rank'
import User from '@/components/user'
import szscore from '@/components/score/szscore'
import szrank from '@/components/score/szrank'
import dkscore from '@/components/score/dkscore'
import dkrank from '@/components/score/dkrank'
import Main from '@/components/main'
Vue.use(Router)

export default new Router({
    routes: [{
            path: '/',
            // redirect: '/login'  
            component: Login
        },
        {
            path: '/main',
            component: Main,
            children: [{
                    path: '/',
                    // component: Rank 
                    redirect: 'rank'
                },
                {
                    path: 'rank',
                    component: Rank
                },
                {
                    path: 'user',
                    component: User
                },
            ]
        },
        // {
        //     path: '/rank',
        //     component: Rank
        // },
        {
            path: '/score/szscore',
            component: szscore
        },
        {
            path: '/score/szrank',
            component: szrank
        },
        {
            path: '/score/dkscore',
            component: dkscore
        },
        {
            path: '/score/dkrank',
            component: dkrank
        },
        {
            path: '/score/analysis',
            component: resolve =>
                require([`../components/score/scoreAnalysis.vue`], resolve),
        },
        {
            path: '/score/wkscore',
            component: resolve =>
                require([`../components/score/wkscore.vue`], resolve),
        },
        {
            path: '/problem/upload',
            component: resolve =>
                require([`../components/problem/upload.vue`], resolve),
        },
        {
            path: '/problem/record',
            component: resolve =>
                require([`../components/problem/record.vue`], resolve),
        }
    ]
})
