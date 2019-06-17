<template>
    <div>
        <x-header>三总成绩趋势</x-header>
        <v-chart :data="data" ref="demo">
            <v-scale x field="test_name"/>
            <v-scale y field="rank"/>
            <v-point :style="{stroke: '#fff', lineWidth: 1}" shape="smooth"/>
            <v-line shape="smooth"/>
        </v-chart>
        <!-- <x-button type="primary" ref="clk" plain @click.native="$refs.demo.rerender()">重新渲染</x-button> -->

        <!-- <input type="button" value="debug" @click="getData"> -->
    </div>
</template>
<script>
import { XHeader, XButton, Loading } from "vux";
import { VChart, VLine, VPoint, VScale, VTooltip } from "vux";

export default {
    data() {
        return {
            data: []
        };
    },
    components: {
        Loading,
        XHeader,
        VChart,
        VPoint,
        VLine,
        VScale,
        VTooltip,
        XButton
    },
    methods: {
        getData() {
            this.data = [];
            this.$vux.loading.show({
                text: "正在加载"
            });
            this.$axios
                .get("/ssm/pastrank", {
                    params: {
                        id: "10000000141",
                        type: "classrank"
                    }
                })
                .then(res => {
                    //;
                    let tmp = res.data;
                    for (var i = 0; i < tmp.length; i++) {
                        let ele = tmp[i];
                        this.data.push({
                            test_name: ele.test_name,
                            rank: ele.rank1
                        });
                    }
                    this.$vux.loading.hide();
                    this.$refs.demo.rerender();
                    // console.log();
                })
                .catch(err => {
                    console.log("err");
                    this.$vux.loading.hide();
                });
        }
    },
    created() {
        this.getData();
    }
};
</script>