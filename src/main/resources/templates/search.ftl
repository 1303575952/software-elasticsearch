<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>软件著作权全文检索系统</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/common.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.4/jquery.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery_lazyload/1.9.7/jquery.lazyload.min.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <style>
        em {
            color: orangered;
        }

        form {
            max-width: 280px;
            max-width: calc(100% - 100px);
        }
    </style>
</head>
<body class="container">
<header>
    <h1><a href="/">软件著作权全文检索系统</a></h1>
    <form action="/s" class="input-group">
        <input name="wd" class="form-control" value='${wd}'>
        <div class="input-group-btn">
            <button type="submit" class="btn btn-primary">搜索</button>
        </div>
    </form>
</header>
    <#if page??>
    <section>
        <p>共找到相关结果${page.total}个，耗时${page.took}ms</p>
    </section>
    <section>
        <#list page.list as movie>
            <div>
                <p>
                    <strong>${movie.software!""}</strong>
                </p>
                <p>${movie.reg_num!"暂无著作权编号"}</p>
                <p>${movie.shortened_form!"暂无版本信息"}</p>
                <p>${movie.company!"暂无相关企业"}</p>
                <p>${movie.reg_pos!"暂无登记机构信息"}</p>
            </div>
        </#list>
    </section>
    <nav aria-label="...">
        <ul class="pager">
            <li class="previous<#if !page.hasPrevious()> disabled</#if>">
                <a href=<#if page.hasPrevious()>"/s?wd=${wd}&pn=${page.pageNo - 1}"<#else>#</#if>><span
                        aria-hidden="true">&larr;</span> 上一页</a>
            </li>
            <li class="next<#if !page.hasNext()> disabled</#if>">
                <a href=<#if page.hasNext()>"/s?wd=${wd}&pn=${page.pageNo + 1}"<#else>#</#if>>下一页 <span
                        aria-hidden="true">&rarr;</span></a>
            </li>
        </ul>
    </nav>
    </#if>
<script>
    $("img.lazy").lazyload();
</script>
</body>
</html>