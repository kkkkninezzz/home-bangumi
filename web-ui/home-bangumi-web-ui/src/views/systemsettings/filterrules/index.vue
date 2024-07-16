<script setup lang="ts">
import EpisodeFilterRulesSettingsForm from "./components/EpisodeFilterRulesSettingsForm.vue";
defineOptions({
  // name 作为一种规范最好必须写上并且和路由的name保持一致
  name: "FilterRulesSettings"
});
</script>

<template>
  <div style="display: flex">
    <div style="flex: 1"></div>

    <el-card style="width: 720px">
      <episode-filter-rules-settings-form />
    </el-card>
    <div style="flex: 1"></div>
  </div>
</template>
