<template>
  <div class="all-container" style="margin-left: 20px;">
    <div style="margin-top: 30px;margin-left: 30px">
      <div>用户列表</div>
      <el-table
        :data="userList.news"
        style="width: 100%;margin-top: 20px">
        <el-table-column
          prop="id"
          label="id"
          width="60">
        </el-table-column>
        <el-table-column
          prop="nickname"
          label="昵称"
          width="100">
        </el-table-column>
        <el-table-column
          prop="tel"
          label="电话"
          width="140">
        </el-table-column>
        <el-table-column
          prop="sex"
          label="性别"
          width="60">
        </el-table-column>

        <el-table-column
          prop="area"
          label="地区">
        </el-table-column>
        <el-table-column
          prop="birthday"
          label="生日"
          width="100">
        </el-table-column>
        <el-table-column
          prop="email"
          label="邮箱"
          width="160">
        </el-table-column>
        <el-table-column
          prop="introduction"
          label="简介">
        </el-table-column>

        <el-table-column
          prop="vision"
          label="视障等级">
        </el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="catHistory(scope.row.id)">浏览记录</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="delUser(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog
        title="历史记录"
        :visible.sync="dialogVisible" width="600px">
        <div id="chart2" class="word-cloud-bg" ></div>
        <el-table :data="historyList.news">
          <el-table-column property="typeName" label="类型" width="150"></el-table-column>
          <el-table-column property="createTime" label="时间"  width="200"></el-table-column>
          <el-table-column property="detail" label="内容"></el-table-column>
        </el-table>
<!--        <el-pagination style="margin-top: 20px" v-if="historyList!=''"-->
<!--          background-->
<!--          layout="prev, pager, next"-->
<!--          :page-size="10"-->
<!--          :total="historyList.page.total"-->
<!--          :page-count="hPage"-->
<!--          @current-change="changeUserPage">-->
<!--        </el-pagination>-->
        <span slot="footer" class="dialog-footer">

    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
      </el-dialog>
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="10"
        :total="userList.page.total"
        :page-count="page"
        @current-change="changeUserPage">
      </el-pagination>
    </div>
<!--    <div id="chart2" class="word-cloud-bg" ></div>-->
  </div>
</template>

<script>
import { webHistory, getUserList, delUser, wordcloud } from '@/api/user'
import Js2WordCloud from 'js2wordcloud'
export default {
  name: 'User',
  data() {
    return {
      page:1,
      wordShow:false,
      wordArr:[],
      size:10,
      userList:[],
      dialogVisible:false,
      gridData: [{
        date: '2022-05-02',
        name: '张',
        address: '江苏省常州市'
      }, {
        date: '2022-05-04',
        name: '张',
        address: '江苏省常州市'
      }, {
        date: '2022-05-01',
        name: '张',
        address: '江苏省常州市'
      }, {
        date: '2022-05-03',
        name: '张',
        address: '江苏省常州市'
      }],
      hPage:1,
      hSize:10,
      historyList:''
    }
  },
  mounted() {
    this.getUserList()
  },
  methods: {
    catHistory(uid){
      this.dialogVisible = true
      webHistory({
        page:this.hPage,
        size:this.hSize,
        uid:uid,
      }).then((res)=>{
        this.historyList = res.data;
        this.wordArr=[];
        function getnum(){
          return parseInt(Math.random()*res.data.news.length)
        }
        res.data.news.forEach(ele=>{
          this.wordArr.push([ele.detail.substring(1,3),getnum()]);
        })
        this.wordCloud(uid);
      })
    },
    //词云
    // 生成词云
    wordCloud (uid) {
      // http://192.168.37.1:9999/api/user/wordcloud?uid=20
      wordcloud(uid).then(response => {
        if (response){
          var list=response.data[0]
          function getnum(){
            return parseInt(Math.random()*40)
          }
          var newArr=[]
          list.forEach(ele=>{
            newArr.push([ele,getnum()]);
          })
          list.forEach(ele=>{
            newArr.push([ele,getnum()]);
          })
          var wc = new Js2WordCloud(document.getElementById('chart2'))
          let option = {
            imageShape: require('../../assets/p3.png'),
            fontSizeFactor: 1,
            tooltip: {
              show: true,
              formatter: function (item) {
                return item[0]
              }
            },
            gridSize:1,
            maxFontSize: 26,
            minFontSize: 12,
            color: 'random-light',
            backgroundColor: 'rgba(0,0,0,.0)',
            list: newArr
          }
          wc.setOption(option)
          window.onresize = function () {
            wc.setOption(option)
          }
        }
      }).catch(error => {
        console.log(error)
      });

    },
    delUser(id){
      let param = {
        uid:id
      }
      delUser(param).then(response => {
        if (response.data === 'success'){
          this.$message({
            message: "删除成功",
            type: "success"
          });
          this.page=1
          this.size=10
          this.getUserList()
        }
      }).catch(error => {
        console.log(error)
      });
    },
    //用户分页
    changeUserPage(page){
      this.page=page
      console.log("切换页码"+page)
      this.getUserList()

    },
    getUserList(){
      let param ={
        page:this.page,
        size:this.size
      }
      getUserList(param).then(response => {
        this.userList = response.data

      }).catch(error => {
        console.log(error)
      });
    }
  }
}
</script>

<style scoped>

.word-cloud-bg{
  width: 450px;
  height: 384px;
  background: url('../../assets/p3.png');
  background-size: 100% 100%;
}

</style>
