import { isString, isEmpty } from "@pureadmin/utils";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import {
  useRouter,
  useRoute,
  type LocationQueryRaw,
  type RouteParamsRaw
} from "vue-router";

export function useDetailPage() {
  const route = useRoute();
  const router = useRouter();
  const getParameter = isEmpty(route.params) ? route.query : route.params;

  function toDetailPage(
    parameter: LocationQueryRaw | RouteParamsRaw
  ) {
    
    // ⚠️ 这里要特别注意下，因为vue-router在解析路由参数的时候会自动转化成字符串类型，比如在使用useRoute().route.query或useRoute().route.params时，得到的参数都是字符串类型
    // 所以在传参的时候，如果参数是数字类型，就需要在此处 toString() 一下，保证传参跟路由参数类型一致都是字符串，这是必不可少的环节！！！
    Object.keys(parameter).forEach(param => {
      if (!isString(parameter[param])) {
        parameter[param] = parameter[param].toString();
      }
    });

    let title: string;
    let routePath: string;
    let routeName: string;
    // 如果是编辑
    title = `Rss番剧详情`;
      routePath = `/my-bangumi/rss-bangumi/detail/:id`;
      routeName = "RssBangumiCardDetailPage";
    // // 保存信息到标签页
    // useMultiTagsStoreHook().handleTags("push", {
    //   path: `/rss-bangumi/edit`,
    //   name: "RssBangumiCardEditPage",
    //   query: parameter,
    //   meta: {
    //     title: title,
    //     // 最大打开标签数
    //     dynamicLevel: 5
    //   }
    // });
    // // 路由跳转
    // router.push({ name: "RssBangumiCardEditPage", query: parameter });

    // console.log(parameter);
    
    useMultiTagsStoreHook().handleTags("push", {
        path: routePath,
        name: routeName,
        params: parameter,
        meta: {
          title: title
        }
    });
    router.push({ name: routeName, params: parameter });
  }

  // 用于页面刷新，重新获取浏览器地址栏参数并保存到标签页
  const initToDetailPage = () => {
    if (getParameter) toDetailPage(getParameter);
  };

  return { toDetailPage, initToDetailPage, getParameter, router };
}