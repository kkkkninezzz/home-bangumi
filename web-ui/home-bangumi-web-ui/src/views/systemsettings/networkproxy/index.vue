<script setup lang="ts">
import NetworkProxySettingsForm from "./components/NetworkProxySettingsForm.vue";

defineOptions({
  // name 作为一种规范最好必须写上并且和路由的name保持一致
  name: "NetworkProxySettings"
});
</script>

<template>
  <div style="display: flex">
    <div style="flex: 1"></div>

    <el-card style="width: 720px">
      <network-proxy-settings-form />
    </el-card>
    <div style="flex: 1"></div>
  </div>
</template>
