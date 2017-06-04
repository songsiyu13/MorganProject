<html>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-6">
            <form class="form-inline" action="/marketDepth">
                <div class="form-group">
                    <label for="goodsName">Goods Name</label>
                    <input type="text" class="form-control" id="goodsName" name="goodsName" placeholder="oil">
                </div>
                <div class="form-group">
                    <label for="goodsDate">Date</label>
                    <input type="month" class="form-control" name="goodsDate" id="goodsDate">
                </div>
                <button type="submit" class="btn btn-default">View Market Depth</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
