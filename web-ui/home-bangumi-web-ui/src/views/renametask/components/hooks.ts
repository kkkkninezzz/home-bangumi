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

  function toDetailPage(parameter: LocationQueryRaw | RouteParamsRaw) {
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
    title = `剧集重命名任务详情`;
    routePath = `/my-bangumi/rename-task/detail/:id`;
    routeName = "EpisodeRenameTaskDetailPage";

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
