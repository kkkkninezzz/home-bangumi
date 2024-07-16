<script setup lang="ts">
import RssBangumiStatisticCard from "./components/RssBangumiStatisticCard.vue";
import RssBangumiEpisodeTimeline from "./components/RssBangumiEpisodeTimeline.vue";
import AlterMessageBox from "./components/AlterMessageBox.vue";

defineOptions({
  name: "Welcome"
});
</script>

<template>
  <el-container>
    <el-header height="200">
      <rss-bangumi-statistic-card />
    </el-header>
    <el-main
      ><el-row :gutter="16">
        <el-col :span="18">
          <rss-bangumi-episode-timeline />
        </el-col>
        <el-col :span="6">
          <alter-message-box />
        </el-col> </el-row
    ></el-main>
  </el-container>
</template>
