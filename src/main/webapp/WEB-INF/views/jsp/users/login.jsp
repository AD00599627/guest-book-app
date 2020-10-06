<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Login Page</title>
</head>
   <jsp:include page="../fragments/header.jsp" />
   <div class="container">
      <c:if test="${not empty msg}">
         <div class="alert alert-${css} alert-dismissible" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
            </button>
            <strong>${msg}</strong>
         </div>
      </c:if>
      <c:choose>
         <c:when test="${login}">
            <h1>Login</h1>
         </c:when>
         <c:otherwise>
            <h1>Login User</h1>
         </c:otherwise>
      </c:choose>
      <br />
      <spring:url value="/users/login" var="userActionUrl" />
      <form:form class="form-horizontal" method="post" modelAttribute="loginNew" action="${userActionUrl}">
         <spring:bind path="email">
            <div class="form-group ${status.error ? 'has-error' : ''}">
               <label class="col-sm-2 control-label">Email</label>
               <div class="col-sm-10">
                  <form:input path="email" class="form-control" id="email" placeholder="Email" />
                  <form:errors path="email" class="control-label" />
               </div>
            </div>
         </spring:bind>
         <spring:bind path="password">
            <div class="form-group ${status.error ? 'has-error' : ''}">
               <label class="col-sm-2 control-label">Password</label>
               <div class="col-sm-10">
                  <form:password path="password" class="form-control" id="password" placeholder="password" />
                  <form:errors path="password" class="control-label" />
               </div>
            </div>
         </spring:bind>
         <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
               <button type="submit" class="btn-lg btn-primary pull-right">Login</button>
            </div>
         </div>
      </form:form>
   </div>
   <jsp:include page="../fragments/footer.jsp" />
   </body>
</html>