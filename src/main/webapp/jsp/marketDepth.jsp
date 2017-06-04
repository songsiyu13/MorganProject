<%@ page import="com.entity.MarketDepth" %>
<%@ page import="com.entity.MarketCommodity" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.math.BigDecimal" %><%--
  Created by IntelliJ IDEA.
  User: 滩涂上的芦苇
  Date: 2017/6/4
  Time: 21:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

    <script src="/js/echarts.min.js"></script>

    <title>Market Depth</title>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div id="main" style="width: 100%; height: 300px"></div>
    </div>
</div>

<script type="text/javascript">
    var name = '<%=request.getAttribute("name")%>';
    var data = [];
    var data2 = [];
    <%
    MarketDepth marketDepth = (MarketDepth) request.getAttribute("marketDepth");
    if(marketDepth != null)
        {
            BigDecimal maxBuy = null,minSell = null;
            if(marketDepth.getBuyMarketDepth()!=null){
                Collections.reverse(marketDepth.getBuyMarketDepth());
                for (MarketCommodity marketCommodity :marketDepth.getBuyMarketDepth()) {
                    %>data.push(<%=marketCommodity.getPrice()%>);data2.push(<%=marketCommodity.getQuantity()%>);<%
                    maxBuy = marketCommodity.getPrice();
                }
            }
            if(marketDepth.getSellMarketDepth() !=null || marketDepth.getSellMarketDepth().size() != 0)
            {
                minSell = marketDepth.getSellMarketDepth().get(0).getPrice();
                if(maxBuy != null)
                {
                    BigDecimal mid = maxBuy.add(minSell).divide(new BigDecimal(2));
                    %>data.push(<%=mid%>);data2.push(<%=0%>);<%
                }
                for (MarketCommodity marketCommodity :marketDepth.getSellMarketDepth()) {
                   %>data.push(<%=marketCommodity.getPrice()%>);data2.push(<%=marketCommodity.getQuantity()%>);<%
                }
            }
        }
    %>
</script>
<script src="/js/show.js"></script>
</body>
</html>
