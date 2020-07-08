<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Login</title>
</head>
<body>
	<div align="center">
		<h1>Admin Login</h1>
		<form id="contact" action="index" method="POST">
			<table>

				<tr>
					<td>ID</td>
					<td><input id="name" name="id" type="text" />
					<td>
				</tr>
				<tr>
					<td>Password</td>
					<td><input id="password" name="password" type="password" />
					<td>
				<tr>
					<td colspan="2" align="center"><input type="submit"
						value="Login" /></td>
				</tr>
			</table>

		</form>
			<h3 style="color:red">${message}</h3>

	</div>


</body>
</html>