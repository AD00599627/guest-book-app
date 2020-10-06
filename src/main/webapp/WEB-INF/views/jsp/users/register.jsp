<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<html lang="en">
<head>
  <title>User Register Page</title>
</head>
   <jsp:include page="../fragments/header.jsp" />
   <div class="container">
      <c:choose>
         <c:when test="${register}">
            <h1>register</h1>
         </c:when>
         <c:otherwise>
            <h1>Register User</h1>
         </c:otherwise>
      </c:choose>
      <br />
      <spring:url value="/users/register" var="userActionUrl" />
      <form:form class="form-horizontal" method="post" modelAttribute="registerNew" action="${userActionUrl}">
         <%-- <form:hidden path="id" /> --%>
         <spring:bind path="name">
            <div class="form-group ${status.error ? 'has-error' : ''}">
               <label class="col-sm-2 control-label">Name</label>
               <div class="col-sm-10">
                  <form:input path="name" type="text" class="form-control " id="name" placeholder="Name" />
                  <form:errors path="name" class="control-label" />
               </div>
            </div>
         </spring:bind>
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
         <spring:bind path="confirmPassword">
            <div class="form-group ${status.error ? 'has-error' : ''}">
               <label class="col-sm-2 control-label">confirm Password</label>
               <div class="col-sm-10">
                  <form:password path="confirmPassword" class="form-control" id="password" placeholder="password" />
                  <form:errors path="confirmPassword" class="control-label" />
               </div>
            </div>
         </spring:bind>
         <spring:bind path="contact">
            <div class="form-group ${status.error ? 'has-error' : ''}">
               <label class="col-sm-2 control-label">Contact</label>
               <div class="col-sm-10">
                  <%-- <form:input path="name" type="text" class="form-control " id="name" placeholder="Name" /> --%>
                  <form:input path="contact" class="form-control" id="contact" placeholder="contact" />
                  <form:errors path="contact" class="control-label" />
               </div>
            </div>
         </spring:bind>
         <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
               <button type="submit" class="btn-lg btn-primary pull-right">Sign Up</button>
            </div>
         </div>
      </form:form>
   </div>
   <jsp:include page="../fragments/footer.jsp" />
   </body>
</html>