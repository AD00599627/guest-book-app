<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>User List Page</title>
</head>
   <jsp:include page="../fragments/header.jsp" />
   <body>
      <div class="container">
         <c:if test="${not empty msg}">
            <div class="alert alert-${css} alert-dismissible" role="alert">
               <button type="button" class="close" data-dismiss="alert"
                  aria-label="Close">
               <span aria-hidden="true">&times;</span>
               </button>
               <strong>${msg}</strong>
            </div>
         </c:if>
         <h1>All Users</h1>
         <table class="table table-striped">
         <caption>User List</caption>
          <thead>
               <tr>
                  <th scope="col">Name</th>
                  <th scope="col">Contact</th>
                  <th scope="col">Email</th>
                  <th scope="col">Comments</th>
                  <th scope="col">Action</th>
               </tr>
            </thead>
            <c:forEach var="user" items="${users}">
               <c:if test="${not user.isAdmin=='true'}">
                  <tr>
                     <td>${user.name}</td>
                     <td>${user.contact}</td>
                     <td>${user.email}</td>
                     <td>${user.comments}</td>
                     <td>
                        <spring:url value="/users/${user.id}/edit" var="editUrl" />
                        <spring:url value="/users/${user.id}/delete" var="deleteUrl" />
                        <spring:url value="/users/${user.id}/approve" var="approveUrl" />
                        <c:choose>
                           <c:when test="${user.isApproved=='true'}">
                              <button class="btn btn-primary" onclick="location.href='${approveUrl}'" disabled="disabled">Approved</button>
                              <button class="btn btn-info" onclick="location.href='${editUrl}'" disabled="disabled">Edit</button>
                           </c:when>
                           <c:otherwise>
                              <button class="btn btn-primary" onclick="location.href='${approveUrl}'">Approve</button>
                              <button class="btn btn-info" onclick="location.href='${editUrl}'">Edit</button>
                           </c:otherwise>
                        </c:choose>
                        <button class="btn btn-danger" onclick="this.disabled=true;post('${deleteUrl}')">Delete</button>
                     </td>
                  </tr>
               </c:if>
            </c:forEach>
         </table>
      </div>
      <jsp:include page="../fragments/footer.jsp" />
   </body>
</html>