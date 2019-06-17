<template>
    <div>
        <x-header>五科成绩</x-header>
        <div>
            <group label-width="5em">
                <popup-picker :title="title1" :data="list2" v-model="value"></popup-picker>
            </group>
            <x-button type="primary" @click.native="rerender()">确认</x-button>
        </div>
        <div>
            <ve-histogram :data="chartData" :legend-visible="false"></ve-histogram>
        </div>
        
    </div>
</template>
<script>
import { XHeader, Group, PopupPicker, XButton } from "vux";
export default {
    components: {
        XHeader,
        Group,
        PopupPicker,
        XButton
    },
    data() {
        return {
            chartData: {
                columns: ["lesson", "score"],
                rows: []
            },
            value: ["test1"],
            list1: [["语文", "数学", "英语", "物理", "化学"]],
            list2: [
                ["test1", "test2", "test3", "test4", "test5", "test6", "test7"]
            ],
            title1: "选择考试",
            
        };
    },
    methods: {
        rerender() {
            const _this = this;
            _this.$axios
                .get("/ssm/classlessonscore", {
                    params: {
                        test: _this.value[0],
                        stuid: "10000000141"
                    }
                })
                .then(res => {
                    _this.chartData.rows = [];
                    res.data.forEach(ele => {
                        if (_this.list1[0].indexOf(ele.lesson) != -1) {
                            _this.chartData.rows.push(ele);
                        }
                    });
                    console.log(_this.chartData);
                })
                .catch(err => {});
        }
    },
    created() {
        this.rerender();
    }
};
</script>