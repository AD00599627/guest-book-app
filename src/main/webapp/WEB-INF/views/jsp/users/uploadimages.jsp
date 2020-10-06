<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Upload Image Page</title>
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
      <h1>Upload Image</h1>
      <br />
      <spring:url value="/users/uploadimages" var="userActionUrl" />
      <form:form class="form-horizontal" method="post" modelAttribute="file" enctype="multipart/form-data" action="${userActionUrl}">
         <input type="file" name="file" /><br><br>
         <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
               <button class="btn btn-primary  pull-right" onclick="location.href='${userActionUrl}'">Upload Image</button>
            </div>
         </div>
      </form:form>
   </div>
   <jsp:include page="../fragments/footer.jsp" />
   </body>
</html>