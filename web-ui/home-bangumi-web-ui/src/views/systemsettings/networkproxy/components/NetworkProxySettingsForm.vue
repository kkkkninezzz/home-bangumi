<script setup lang="ts">
import { ref, Ref, onBeforeMount, computed } from "vue";
import "plus-pro-components/es/components/form/style/css";
import {
  type PlusColumn,
  type FieldValues,
  PlusForm
} from "plus-pro-components";

import { NetProxyTypeEnum } from "../enums";

import {
  NetworkProxySettingsResp,
  UpdateNetworkProxySettingsReq,
  HttpProxySettingsDto,
  Socks5ProxySettingsDto,
  getNetworkProxySettings,
  updateNetworkProxySettings
} from "@/api/systemSettings";
import { message } from "@/utils/message";
import { ComputedRef } from "vue";

defineOptions({
  name: "NetworkProxySettingsForm"
});

const submitLoading = ref(false);
// 网络配置的表单状态
const settingsState = ref<FieldValues>({
  enable: false,
  type: NetProxyTypeEnum.HTTP, // 网络代理类型
  httpHost: "", // http类型的host
  httpPort: 0, // http类型的port
  socks5Host: "", // socks5类型的host
  socks5Port: 0, // socks5类型的port
  socks5Username: "", // socks5类型的用户名
  socks5Password: "" // socks5类型的密码
});

// 初始化网络代理设置的表单
async function initSettingsForm() {
  const resp: NetworkProxySettingsResp = await getNetworkProxySettings();
  if (!resp.success) {
    return;
  }
  setSettingsState(resp);
}

function setSettingsState(resp: NetworkProxySettingsResp) {
  settingsState.value.enable = resp.data.enable;
  settingsState.value.type =
    resp.data.networkProxyType ?? NetProxyTypeEnum.HTTP;
  settingsState.value.httpHost = resp.data.httpProxySettings?.host ?? "";
  settingsState.value.httpPort = resp.data.httpProxySettings?.port ?? 0;

  settingsState.value.socks5Host = resp.data.socks5ProxySettings?.host ?? "";
  settingsState.value.socks5Port = resp.data.socks5ProxySettings?.port ?? 0;
  settingsState.value.socks5Username =
    resp.data.socks5ProxySettings?.username ?? "";
  settingsState.value.socks5Password =
    resp.data.socks5ProxySettings?.password ?? "";
}

onBeforeMount(() => {
  initSettingsForm();
});

const hideHttpSettings: ComputedRef<boolean> = computed(
  () =>
    !settingsState.value.enable ||
    settingsState.value.type == 0 ||
    settingsState.value.type != NetProxyTypeEnum.HTTP
);

const hideSocks5Settings: ComputedRef<boolean> = computed(
  () =>
    !settingsState.value.enable ||
    settingsState.value.type == 0 ||
    settingsState.value.type != NetProxyTypeEnum.SOCKS5
);

const settingsColumns: PlusColumn[] = [
  {
    label: "网络代理",
    width: 100,
    prop: "enable",
    valueType: "switch",
    colProps: {
      span: 24
    }
  },
  {
    label: "代理类型",
    prop: "type",
    valueType: "select",
    hideInForm: computed(() => !settingsState.value.enable),
    colProps: {
      span: 24
    },
    options: [
      {
        label: "NONE",
        value: NetProxyTypeEnum.NONE,
        color: "blue"
      },
      {
        label: "HTTP",
        value: NetProxyTypeEnum.HTTP,
        color: "blue"
      },
      {
        label: "SOCKS5",
        value: NetProxyTypeEnum.SOCKS5,
        color: "blue"
      }
    ]
  },
  {
    label: "HTTP host",
    width: 100,
    fieldProps: {
      placeholder: "请输入host，如 127.0.0.1"
    },
    prop: "httpHost",
    valueType: "copy",
    hideInForm: hideHttpSettings,
    colProps: {
      span: 18
    }
  },
  {
    label: "Port",
    width: 100,
    prop: "httpPort",
    fieldProps: {
      placeholder: "请输入端口",
      min: 0
    },
    valueType: "input-number",
    //hasLabel: false,
    hideInForm: hideHttpSettings,
    colProps: {
      span: 6
    }
  },
  {
    label: "SOCKS5 host",
    width: 100,
    fieldProps: {
      placeholder: "请输入host，如 127.0.0.1"
    },
    prop: "socks5Host",
    valueType: "copy",
    hideInForm: hideSocks5Settings,
    colProps: {
      span: 18
    }
  },
  {
    label: "Port",
    width: 100,
    prop: "socks5Port",
    fieldProps: {
      placeholder: "请输入端口",
      min: 0
    },
    valueType: "input-number",
    //hasLabel: false,
    hideInForm: hideSocks5Settings,
    colProps: {
      span: 6
    }
  },
  {
    label: "用户名",
    width: 100,
    prop: "socks5Username",
    valueType: "copy",
    hideInForm: hideSocks5Settings,
    colProps: {
      span: 24
    }
  },
  {
    label: "密码",
    width: 100,
    prop: "socks5Password",
    valueType: "copy",
    fieldProps: { type: "password", showPassword: true },
    hideInForm: hideSocks5Settings,
    colProps: {
      span: 24
    }
  }
];

const handleSubmit = async (values: FieldValues) => {
  let httpProxySettings: HttpProxySettingsDto = null;
  if (settingsState.value.httpHost != "") {
    httpProxySettings = {
      host: (settingsState.value.httpHost as string).trim(),
      port: settingsState.value.httpPort as number
    };
  }

  let socks5ProxySettings: Socks5ProxySettingsDto = null;
  if (settingsState.value.socks5Host != "") {
    socks5ProxySettings = {
      host: (settingsState.value.socks5Host as string).trim(),
      port: settingsState.value.socks5Port as number,
      username: (settingsState.value.socks5Username as string).trim(),
      password: (settingsState.value.socks5Password as string).trim()
    };
  }
  const req: UpdateNetworkProxySettingsReq = {
    enable: settingsState.value.enable as boolean,
    networkProxyType: settingsState.value.type
      ? (settingsState.value.type as number)
      : NetProxyTypeEnum.NONE,
    httpProxySettings: httpProxySettings,
    socks5ProxySettings: socks5ProxySettings
  };

  const resp: NetworkProxySettingsResp = await updateNetworkProxySettings(req);

  if (!resp.success) {
    return;
  }
  message("保存成功", { type: "success" });
  setSettingsState(resp);
};
</script>
<template>
  <PlusForm
    label-position="top"
    v-model="settingsState"
    :columns="settingsColumns"
    :row-props="{ gutter: 20 }"
    footerAlign="right"
    @submit="handleSubmit"
  >
  </PlusForm>
</template>
