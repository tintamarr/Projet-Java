<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/views/common/head.jsp"%>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <!-- Left side column. contains the logo and sidebar -->
    <%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Utilisateurs
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/users/create">Ajouter</a>
            </h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <div class="box">
                        <div class="box-body no-padding">
                            <table class="table table-striped">
                                <tr>
                                    <th style="width: 10px">#</th>
                                    <th>Nom</th>
                                    <th>Prenom</th>
                                    <th>Email</th>
                                    <th>Naissance</th>
                                    <th>Action</th>
                                </tr>
                                <tr>
                                <tr>

                                <c:forEach items="${users}" var="users">
                                    <td>${users.id}.</td>
                                    <td>${users.nom}</td>
                                    <td>${users.prenom}</td>
                                    <td>${users.email}</td>
                                    <td>${users.naissance}</td>

                                    <td>
                                        <div class="row">
                                            <div class="col-xs-3">
                                                <a class="btn btn-primary disabled" href="users-detail.html">
                                                    <i class="fa fa-play"></i>
                                                </a>
                                            </div>
                                            <div class="col-xs-3">
                                                <a class="btn btn-success disabled" href="#">
                                                    <i class="fa fa-edit"></i>
                                                </a>
                                            </div>
                                            <div class="col-xs-3">
                                                <form action="users/delete" method="post">
                                                    <button type="submit" class="btn btn-danger" name="userId" value="${users.id}">
                                                        <i class="fa fa-trash"></i>
                                                    </button>
                                                </form>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col -->
            </div>
        </section>
        <!-- /.content -->
    </div>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
<!-- ./wrapper -->

<%@ include file="/WEB-INF/views/common/js_imports.jsp" %>
</body>
</html>
