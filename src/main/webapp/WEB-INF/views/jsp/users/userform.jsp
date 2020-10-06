<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>User Form Page</title>
</head>
   <jsp:include page="../fragments/header.jsp" />
   <div class="container">
      <c:choose>
         <c:when test="${userForm['new']}">
            <h1>Guest Book Form</h1>
         </c:when>
         <c:otherwise>
            <h1>Add Reviews or Upload Image</h1>
         </c:otherwise>
      </c:choose>
      <br />
      <spring:url value="/users/userform" var="userActionUrl" />
      <spring:url value="/users/upload" var="uploadUrl" />
      <spring:url value="/users/downloadimages" var="dowmloadImageUrl" />
      <form:form class="form-horizontal" method="post" modelAttribute="userForm"  action="${userActionUrl}" >
         <form:hidden path="id" />
         <spring:bind path="name">
            <div class="form-group ${status.error ? 'has-error' : ''}">
               <label class="col-sm-2 control-label">Name</label>
               <div class="col-sm-10">
                  <form:input path="name" type="text" class="form-control " id="name" disabled="true" placeholder="Name" />
                  <form:errors path="name" class="control-label" />
               </div>
            </div>
         </spring:bind>
         <spring:bind path="comments">
            <div class="form-group ${status.error ? 'has-error' : ''}">
               <label class="col-sm-2 control-label">Comments</label>
               <div class="col-sm-10">
                  <form:textarea path="comments" rows="5" class="form-control" id="comments" placeholder="comments" />
                  <form:errors path="comments" class="control-label" />
               </div>
            </div>
         </spring:bind>
         <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
               <button class="btn btn-primary  pull-right" name="addreviews" onclick="location.href='${addreviewUrl}'">Add Reviews</button>
            </div>
         </div>
      </form:form>
  <form method="get" action="${dowmloadImageUrl}">
   <button type="submit">Download Image!</button>
</form>
      <form:form class="form-horizontal" method="post" modelAttribute="userForm"  action="${uploadUrl}" >
         <form:hidden path="id" />
         <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
               <button class="btn btn-primary  pull-right" name="uploadimage" onclick="location.href='${uploadUrl}'">Upload Image Below 1MB</button>
            </div>
         </div>
      </form:form>
   </div>
   <jsp:include page="../fragments/footer.jsp" />
   </body>
</html>