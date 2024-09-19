<template>
  <el-row style="margin: 20px;">
    <el-button type="primary" style="margin: 5px;" @click="addUserClick">添加新闻</el-button>
    <el-table
      :data="newsList.news"
      style="width: 100%;margin-top: 20px;overflow-x: auto;">

      <el-table-column
        prop="id"
        width="100"
        label="编号">
      </el-table-column>
      <el-table-column
        prop="media"
        width="150"
        label="来源">
      </el-table-column>

      <el-table-column
        prop="nclass"
        width="150"
        label="类型">
      </el-table-column>
      <el-table-column
        prop="title"
        label="标题">
      </el-table-column>

      <el-table-column
        prop="time"
        label="时间"
        width="200">
      </el-table-column>

      <!--      <el-table-column-->
      <!--        prop="bankNumber"-->
      <!--        label="地区"-->
      <!--        idth="200">-->
      <!--      </el-table-column>-->


      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="editUser(scope.$index, scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="delNews(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      layout="prev, pager, next"
      :page-size="10"
      :total="newsList.content"
      :page-count="page"
      @current-change="changeUserPage">
    </el-pagination>

    <!--添加或者修改用户-->
    <el-dialog :title="userTitle" top="5%" :visible.sync="dialogVisible"  @close="closeDialog">

      <el-form :model="news" ref="userForm">
        <el-form-item label="新闻图片" :label-width="formLabelWidth">
          <el-upload
            class="avatar-uploader"
            action="http://36.138.201.130:9999/api/news/file"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="news.image !=''" :src="news.image" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>

        </el-form-item>

        <el-form-item label="新闻类型" :label-width="formLabelWidth">
          <el-select v-model="news.nclass" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.label">
            </el-option>
          </el-select>

        </el-form-item>

        <el-form-item label="来源" :label-width="formLabelWidth">
          <el-input v-model="news.media" autocomplete="off" ></el-input>
        </el-form-item>
        <el-form-item  label="新闻标题" :label-width="formLabelWidth">
          <el-input v-model="news.title" autocomplete="off" ></el-input>
        </el-form-item>

        <el-form-item label="时间" :label-width="formLabelWidth">
          <el-date-picker formatter="yyyy-MM-dd HH:mm:ss"
            v-model="news.time"
            type="datetime"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="内容" :label-width="formLabelWidth">
          <el-input :rows="5"  type="textarea" v-model="news.text" autocomplete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="updateUser">保存</el-button>
      </div>
    </el-dialog>
  </el-row>
</template>

<script>
import { getNewsPageWeb, addNews, updNews, delNews,} from '@/api/user'
import {getDepartmentNames} from '@/api/department';
import {parseTime} from "@/utils";
export default {
  name: 'newsManager',
  data(){
    return{
      activeName:"account",
      dialogVisible:false,
      formLabelWidth: "150px",
      userInfo:{
        content:[],//数据
        currentPage:0,//当前页数
        totalElements:0,//总条数
        totalPage:0//总页数
      },
      //信息
      user:{
        id:null,
        name:"",
        note:"",
        money:null,
        startTime:"",
        endTime:"",
        department:{
          id:null,
          name:""
        }
      },
      showUser:false,
      isAddUser:true,
      userTitle:"添加新闻信息",
      pageSize:10,
      departmentNames:[],
      news:{
        image:''
      },
      options: [{
        value: '1',
        label: '社会・法治'
      }, {
        value: '2',
        label: '经济・科技'
      }, {
        value: '3',
        label: '文旅・体育'
      }, {
        value: '4',
        label: '观点'
      }, {
        value: '5',
        label: '国际'
      }, {
        value: '6',
        label: '军事'
      }, {
        value: '7',
        label: '港澳'
      }, {
        value: '8',
        label: '台湾频道'
      }, {
        value: '9',
        label: '教育'
      }, {
        value: '10',
        label: '党网时政'
      }, {
        value: '11',
        label: '健康生活'
      }],
      page:1,
      size:10,
      newsList:[],
      type:'',
      imageUrl:""
    };
  },
  mounted(){
    this.getUserPages();
    this.getDepartmentNames();
  },
  methods:{
    handleAvatarSuccess(res, file) {

      this.news.image =res.data
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 5;
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!');
      }
      return isLt2M;
    },

    timeFormat(Time) {
      //日期格式化
      var time = new Date(Time)
      var year = time.getFullYear()
      var month = time.getMonth() + 1
      var date = time.getDate()
      var hours = time.getHours()
      var minutes = time.getMinutes()
      var seconds = time.getSeconds()
      function add0(m) {
        return m < 10 ? '0' + m : m
      }
      var startTime = add0(year) + '-' + add0(month) + '-' + add0(date) + ' ' + add0(hours) + ':' + add0(minutes) + ':' + add0(seconds)
      return startTime
    },

    getUserPages(){
      let param = {
        page:this.page,
        size:this.size,
        nclass: '',
        keyword:''
      }
      getNewsPageWeb(param).then(response => {
        this.newsList = response.data

      }).catch(error => {
        console.log(error)
      });
    },
    //用户分页
    changeUserPage(page){
      this.page=page
      console.log("切换页码"+page)
      this.getUserPages();
    },
    addUserClick(){
      this.type = 1
      this.dialogVisible = true;

    },
    //添加
    addUser(){
     let t = this.timeFormat(this.news.time)
      if (this.news.image===''){
        this.$message({
          message: "请上传新闻图片",
          type: "warning"
        });
        return
      }
      this.news.time = t
      addNews(this.news).then(response => {
        this.$message({
          message: "添加新闻成功",
          type: "success"
        });
        this.dialogVisible = false;
        this.getUserPages();

      }).catch(error => {
        console.log(error)
      });
    },
    updNews(){
      updNews(this.news).then(response => {
        this.$message({
          message: "修改成功",
          type: "success"
        });
        this.dialogVisible = false;

      }).catch(error => {
        console.log(error)
      });
    },
    editUser(index,row){
      this.dialogVisible = true;
      this.news = row
      this.type = 2

      this.userTitle="新闻信息";
    },
    //修改用户信息
    updateUser(){
      if (this.type===1){
        this.addUser()
      }
      if (this.type===2){
        this.updNews()

      }

    },
    delNews(id){
      delNews({
        nid:id
      }).then(response => {
        this.$message({
          message: "删除成功",
          type: "success"
        });
        this.page=1
        this.size=10
        this.getUserPages()
      }).catch(error => {
        console.log(error)
      });
      this.$forceUpdate();

    },
    getDepartmentNames(){
      getDepartmentNames().then(response => {
        this.departmentNames = response.data;
      }).catch(error => {
        console.log(error)
      });
    },
    closeDialog(){
      this.dialogVisible = false
      this.news={}
    }
  },
  filters:{
    formatDate:function(time) {
      if(time!=null&&time!=="")
      {
        // var date = new Date(time);
        return parseTime(time, '{y}-{m}-{d}');
      }else{
        return "";
      }
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

</style>
