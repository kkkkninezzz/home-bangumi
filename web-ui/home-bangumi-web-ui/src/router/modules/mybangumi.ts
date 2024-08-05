export default {
  path: "/my-bangumi",
  redirect: "/my-bangumi/rss-bangumi/index",
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
        title: "Rss番剧"
      }
    },
    {
      path: "/my-bangumi/rss-bangumi/sub-page",
      name: "RssBangumiSubPage",
      redirect: "/my-bangumi/rss-bangumi/index",
      meta: {
        title: "Rss番剧"
      },
      children: [
        {
          path: "/my-bangumi/rss-bangumi/detail/:id",
          component: () =>
            import(
              "@/views/rssbangumi/components/RssBangumiCardDetailPage.vue"
            ),
          name: "RssBangumiCardDetailPage",
          meta: {
            // 不在menu菜单中显示
            showLink: false,
            activePath: "/my-bangumi/rss-bangumi/index"
          }
        }
      ]
    },
    {
      path: "/my-bangumi/rename-task/index",
      name: "EpisodeRenameTask",
      component: () => import("@/views/renametask/index.vue"),
      meta: {
        icon: "mdi:rename-outline",
        title: "剧集重命名",
        showParent: true
      }
    },
    {
      path: "/my-bangumi/rename-task/sub-page",
      name: "EpisodeRenameTaskSubPage",
      redirect: "/my-bangumi/rename-task/index",
      meta: {
        title: "剧集重命名"
      },
      children: [
        {
          path: "/my-bangumi/rename-task/detail/:id",
          component: () =>
            import(
              "@/views/renametask/components/EpisodeRenameTaskDetailPage.vue"
            ),
          name: "EpisodeRenameTaskDetailPage",
          meta: {
            // 不在menu菜单中显示
            showLink: false,
            activePath: "/my-bangumi/rename-task/index"
          }
        }
      ]
    }
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
