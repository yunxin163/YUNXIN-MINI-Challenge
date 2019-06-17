<template>
    <div>
        <x-header >三总排名趋势</x-header>
        <v-chart :data="data" ref="demo1">
            <v-scale x field="test_name"/>
            <v-scale y field="rank"/>
            <v-point :style="{stroke: '#fff', lineWidth: 1}" shape="smooth"/>
            <v-line shape="smooth"/>
        </v-chart>
    </div>
</template>
<script>
import {XHeader,Loading} from 'vux'
import { VChart, VLine, VPoint, VScale, VTooltip } from "vux";
export default {
    data() {
        return {
            data: []
        };
    },
    components:{
        XHeader,
        VChart,
        VPoint,
        VLine,
        VScale,
        VTooltip,
        Loading
    },
    methods: {
        getData() {
            this.$vux.loading.show({
                text: '正在加载'
            })
            this.$axios
                .get("/ssm/pastrank",{
                    params:{
                        id:"10000000141",
                        type:"classrank"
                    }
                })
                .then(res => {
                    // this.data = [];
                    let tmp = res.data;
                    for(var i=0;i<tmp.length;i++){
                        let ele=tmp[i];
                        this.data.push({
                            "test_name": ele.test_name,
                            "rank": ele.rank1
                        });
                    }
                    this.$vux.loading.hide()
                    this.$refs.demo1.rerender()
                })
                .catch(err => {
                    this.$vux.loading.hide()
                    console.log("err");
                })
        }
    },
    created() {
        this.getData();
    }
}
</script>