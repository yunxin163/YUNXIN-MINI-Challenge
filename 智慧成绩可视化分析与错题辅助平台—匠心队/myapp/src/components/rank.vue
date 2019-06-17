<template>
    <div>
        <x-header :left-options="{showBack: false}">智慧成绩分析系统</x-header>

        <grid :cols="3" :show-vertical-dividers="true">
            <grid-item label="三总成绩" link="/score/szscore">
                <img slot="icon" src="../assets/score.png">
            </grid-item>
            <grid-item label="三总排名" link="/score/szrank">
                <img slot="icon" src="../assets/rank.png">
            </grid-item>
            <grid-item label="成绩查询" link="/score/dkscore">
                <img slot="icon" src="../assets/rank1.png">
            </grid-item>
            <grid-item label="单科排名" link="/score/dkrank">
                <img slot="icon" src="../assets/rank2.png">
            </grid-item>
            <grid-item label="成绩分析" link="/score/analysis">
                <img slot="icon" src="../assets/fenxi.png">
            </grid-item>
            <grid-item label="五科成绩" link="/score/wkscore">
                <img slot="icon" src="../assets/wuke.png">
            </grid-item>
            <grid-item label="错题上传" link="/problem/upload">
                <img slot="icon" src="../assets/upload.png">
            </grid-item>
            <grid-item label="上传记录" link="/problem/record">
                <img slot="icon" src="../assets/history.png">
            </grid-item>
            <grid-item label="错题查看" >
                <img slot="icon" src="../assets/scan.png">
            </grid-item>
            <!-- <grid-item label="错题下载" > -->
                <!-- <img slot="icon" src="../assets/download.png"> -->
            <!-- </grid-item> -->
        </grid>
    </div>
</template>
<script>
import { XHeader,Grid, GridItem, GroupTitle } from "vux";

export default {
    components: {
        Grid,
        GridItem,
        GroupTitle,
        XHeader
    },
    methods: {
        test(){
            alert("111");
        }
    }
};
</script>

<style scoped>
.grid-center {
    display: block;
    text-align: center;
    color: #666;
}
.weui-grids {
    background-color: #fff;
}
</style>