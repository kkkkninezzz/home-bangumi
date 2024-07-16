export default {
  path: "/my-bangumi",
  meta: {
    icon: "ep:expand",
    title: "我的番剧"
  },
  children: [
    {
      path: "/my-bangumi/rss-bangumi/index",
      name: "RssBangumi",
      component: () => import("@/views/rssbangumi/index.vue"),
      meta: {
        icon: "ep:link",
        title: "Rss番剧",
        showParent: true
      }
    },
    {
      path: "/my-bangumi/rss-bangumi/detail/:id",
      component: () => import("@/views/rssbangumi/components/RssBangumiCardDetailPage.vue"),
      name: "RssBangumiCardDetailPage",
      meta: {
        // 不在menu菜单中显示
        showLink: false,
        activePath: "/my-bangumi/rss-bangumi/index"
      }
    },
    {
      path: "/my-bangumi/rss-bangumi/new",
      component: () => import("@/views/rssbangumi/components/RssBangumiCardDetailPage.vue"),
      name: "RssBangumiCardNewPage",
      meta: {
        // 不在menu菜单中显示
        showLink: false,
        activePath: "/my-bangumi/rss-bangumi/index"
      }
    },
    // {
    //   path: "/my-bangumi/bangumi-collection/index",
    //   name: "BangumiCollection",
    //   component: () => import("@/views/bangumicollection/index.vue"),
    //   meta: {
    //     icon: "ep:files",
    //     title: "番剧合集"
    //   }
    // },
  ]
};