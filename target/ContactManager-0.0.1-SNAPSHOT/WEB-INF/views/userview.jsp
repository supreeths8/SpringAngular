<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contact Manager</title>
</head>
<body>
	<form:form method="post" modelAttribute="userContact">
		<div align="center">
			<h1>Welcome ${userContact.name}</h1>
			<h2>Account Details</h2>
			<br> <br>
			<table border="1" cellpadding="3">
				<tr>
					<th>ID</th>
					<th>Name</th>
					<th>Email</th>
					<th>Address</th>
					<th>Phone</th>
					<th>Balance</th>
				</tr>
				<tr>
					<td>${userContact.id}</td>
					<td>${userContact.name}</td>
					<td>${userContact.email}</td>
					<td>${userContact.address}</td>
					<td>${userContact.phone}</td>
					<td>${userBalance.amount}</td>
				</tr>
			</table>
		</div>
		<br>
		<div align="center">
			<h3 style="color:red">${message}</h3>

			<table>

				<tr>
					<td colspan="2" align="left"><input type="text"
						id="withdrawAmount" name="withdrawAmount"></td>
					<td colspan="2" align="right"><button type="submit"
							formaction="/ContactManager/userview/withdraw">Withdraw</button></td>
				</tr>
				<tr>
					<td></td>
					<td colspan="2" align="left"><input type="text"
						id="depositAmount" name="depositAmount"></td>

					<td colspan="2" align="right"><button type="submit"
							formaction="/ContactManager/userview/deposit">Deposit</button></td>
				</tr>
			</table>
		</div>
		<br>
		<br>
		<div align="center">
			<button type="submit" formaction="/ContactManager/userlogout">Logout</button>

		</div>
	</form:form>

</body>
</html>

