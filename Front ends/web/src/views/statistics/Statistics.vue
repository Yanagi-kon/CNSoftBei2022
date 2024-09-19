<template>

  <el-row style="margin-top: 20px;">
    <el-col :span="22" :offset="1">

    </el-col>
    <el-col :span="10" :offset="1">
      <el-card class="box-card">
        <div id="myChart" ref="table" :style="{width: '500px', height: '300px'}"></div>
      </el-card>

    </el-col>
    <el-col :span="10" :offset="1">
      <el-card class="box-card">
        <div id="myChart2" :style="{width: '500px', height: '300px'}"></div>
      </el-card>
    </el-col>
    <el-col :span="10" :offset="1">
      <el-card class="box-card">
        <div id="myChart3" :style="{width: '500px', height: '300px'}"></div>
      </el-card>
    </el-col>
  </el-row>

</template>

<script>
  import {newsByType,distribution} from '@/api/department';

  import 'echarts/map/js/china'

  export default {
    name: 'Statistics',
    data () {
      return {
        msg: 'Welcome to Your Vue.js App',
        form:{
          date1: '',
          date2: '',
        },
        departmentNames:[],
        names:[],
        moneys:[],
        moneys2:[],
        data:'',
        newsNclassBySum:[],
        newsSum:[],
        newsNclassByCount:[],
        newsCount:[],
        distributionData:[
          {"name":"安徽","value":20}
        ]
      }
    },
    mounted(){

      this.getNewsByType();
      this.distribution();
      this.drawLine()
    },
    methods: {
      drawLine(){
        // 基于准备好的dom，初始化echarts实例
        let myChart = this.$echarts.init(document.getElementById('myChart'))
        let myChart2 = this.$echarts.init(document.getElementById('myChart2'))
        let myChart3 = this.$echarts.init(document.getElementById('myChart3'))
        // 绘制图表
        myChart.setOption({
          title: { text: '新闻类别' },
          tooltip: {},
          xAxis: {
            data: this.newsNclassBySum
            // data: ['财经类', '政务类', '你好,世界', '4', '5', '6']
          },
          yAxis: {},
          series: [{
            name: '新闻类别',
            type: 'bar',
            data: this.newsCount,
          }]
        });
        myChart2.setOption({
          title: { text: '新闻浏览量' },
          tooltip: {},
          xAxis: {
            data: this.newsNclassByCount
          },
          yAxis: {},
          series: [{
            name: '新闻浏览量',
            type: 'bar',
            data: this.newsSum
          }],
        });
        const option = {
          title:{
            text:'视障人士地域分布',
            x:'center',
          },
          // 数据和类型
          series: [{
            type:'map',
            map:'china',
            label:{
              show: true,
              color:'red',
              fontSize:10
            },
            // 地图大小倍数
            zoom:1.2,
            data:this.distributionData
          }],
          visualMap:{
            min: 1,
            max: 100,
            text: ['高', '低'],
            realtime: false,
            calculable: true,
            inRange: {
              color: ['lightskyblue', 'yellow', 'orangered']
            }
          }
        }
        // 指定数据项和数据显示
        myChart3.setOption(option);

      },
      getNewsByType(){
        newsByType({}).then(response => {
          this.newsNclassBySum = response.data.newsNclassBySum
          this.newsSum = response.data.newsSum
          this.newsNclassByCount = response.data.newsNclassByCount
          this.newsCount = response.data.newsCount
          this.drawLine();
          this.$refs.table.$forceUpdate();


        }).catch(error => {
          console.log(error)
        });

      },
      distribution(){
        distribution({}).then(response => {
         this.distributionData = response.data
          this.drawLine();
          this.$refs.table.$forceUpdate();


        }).catch(error => {
          console.log(error)
        });

      }
    }
  }
</script>

<style scoped>

</style>
