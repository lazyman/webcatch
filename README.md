# 网页抓取

# 功能介绍

# 分析
页面组成
* 首页
* 分类（category）
参照清单列表处理清单
* 清单列表（ThreadList），分页，所属分类，随机关联新闻
标题-topic，摘要-summary， 时间-time， 日期-date，所属分类-category，随机关联
* 新闻详情（Thread），是否有回复（comment），所属分类，随机关联新闻
标题-topic，内容-content， 时间-time， 日期-date，所属分类-category，附加-，随机关联，上一个-preThread，下一个-nextThread
 * 回复-comment
   作者-author，名称-name， 邮箱-email，标题-topic，内容-content， 时间-time， 日期-date
 * 内容-content-info
   文件大小（file size）-filesize（File size: 344.2 MB）， 发布年-releaseYear， studio， 格式（mp4,avi）-format， 长度-duration， 
   视频参数（960x540, AVC (H.264), 2539kbps ）-video， 音频参数（ 62kbps）-audio，  类型-genres，
   导演-cast，下载地址-downloadurl， 文件数量-filecount（Total size: 2.3 GB in 47 files.）
   
# 设计
缓存所有页面到本地磁盘，数据库记录相对路径，前导路径应可配置。
* 执行流程
  * 下载列表清单
    根据分页规则下载每个页面，防止网站更新出现遗漏，适用于更新频率较长（半天以上更新一次，更新频率小于取完所有列表所消耗的时间）。
    记录上级URL，首次下载时间，检查时间，最近更新时间。（createTime，checkTime，updateTime）
  * 解析页面
    解析出每个新闻单元（Thread），以URL作为每个单元的**主键ID**，存储附带的摘要信息。
    下载新闻单元（Thread），保存到本地路径。
    解析新闻单元（Thread），存入数据库。注意文件大小-size和下载地址-downloadurl
    解析时，应记录对应的下载页面，thread有对应的URL值。
    记录首次解析时间，检查时间，最近更新时间。（createTime，checkTime，updateTime）
    线程池管理，默认线程数2。
* 下载模块
  应保留完整页面内容，包括跨域内容如A.com使用到B.com的内容，域名目录是否逆向建立目录（如www.baidu.com建立的目录为com/baidu/www，主机可能较少，不再分目录），下级目录建立一致的目录体系。
  可配置选择是否下载图片、脚本（img.src，script.src），下载层次（默认3层），是否使用代理（针对哪些地址使用），downPage(url, level)
  下载内容不做处理，存于本地，需要时可通过nginx做URL重写实现文件访问。或者以后专门写个模块做地址替换。
  采用线程池管理，默认线程数3
* 展现
  * 重新展现下载内容
    既显示原来下载的页面，确保网页能离线正常显示，可参照下载模块的**内容处理**描述
  * 显示统计信息
    如按文件大小排序、筛选
    
# 详细设计
 * 系统启动
   * 程序启动入口类
     FetchMain
   * 环境参数
     全局参数定义于入口类FetchMain，局部参数定义于使用场景。
     除了数据库连接信息外，所有参数存储于数据库中，若数据库没有初始化，以设计时的默认参数初始化。
     存储表名-param
     * 全局参数
       下载层次，本地保存目录，是否下载资源（图片-img.src，脚本-script.src），是否使用代理，下载线程数
     * 局部参数
       根据使用场景定义不同的参数
   * 应用入口
     FetchPage.fetch(url)，初始化，然后调用下载模块
 * 下载模块-downloader
   采用线程池管理，取系统参数，默认线程数3
   downPage(parent, url)；
   downPage(parent, url, level)；
   parent-上级地址，url-下载地址，level-层级，默认3（本页面一级，图片资源页一级，展开大图一级）
 * 解析模块-analyzer
   采用线程池管理，取系统参数，默认线程数2
   analyzeHomePage();
   analyzeThreadList();
   analyzeThread();
   使用ThreadDao保存解析结果到数据库
   