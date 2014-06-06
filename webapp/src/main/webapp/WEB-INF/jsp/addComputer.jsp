<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="../../include/header.jsp" />

<script type="text/javascript" src="js/jquery-2.1.0.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>

<!--               JQuery               -->
 <script type="text/javascript">
 $(document).ready(function(){
	 
			jQuery.validator.addMethod("dateValid", function (value,element) { 
				var valid = true;  
				if ( (/^((19|20)\d\d)-(0[1-9]|1[012])-(0[1-9]|[12]\d|3[01]).*/i.test(value)) ) {
					var date = value.split('-');
					var year = parseInt(date[0]);
					var month = parseInt(date[1]);
					var day = parseInt(date[2]);
					if (day > 31 ) {valid = false;} 
					else if ( (day == 31) && ( (month == 2) || (month == 4) || (month == 6) || (month == 9) || (month == 11) ) ) {valid = false;} 
					else if ( ( month == 2 ) && (day > 29) ) {valid = false;} 
					else if ( ( month == 2 ) && (day == 29) && ( (year % 4) != 0) ) {valid = false;} 
				} else {valid = false;}			
				return ( this.optional(element) || valid);
				},"<spring:message code="invalid_date"/>"        
			);								
			
			jQuery.validator.addMethod("dateComparison", function (value,element) {
				if ( ($.trim(value).length > 0) && ($.trim($('#introduced').val()).length > 0) ) { return Date.parse($('#introduced').val()) < Date.parse(value); }
				else {return true;}
				},"<spring:message code="impossible_date"/>"
			);
			
     // Initialisation du plugin
	    $("#form").validate({
	    	rules : {
	        "name":{
	            required: true,
	            maxlength: 255
	        },
	    	"introduced":{
	    		maxlength: 255,
	    		dateValid: true
        	},
	    	"discontinued":{
	    		maxlength: 255,
	    		dateValid: true,
	    		dateComparison: true
        	},
	    	"company":{
	    		required: true,
	    		maxlength: 255
        	}
        	}
	    });
 });
 
 </script>
<!--              /JQuery               -->
<br><br>
<section id="main">

	<h1><spring:message code="addComp"/></h1>
	
	<form:form id="form" action="addComputer" method="POST" commandName="computerDTO">
		<fieldset>
			<div class="clearfix">
				<label for="name"><spring:message code="name"/></label>
				<div class="input">
					<form:input type="text" name="name" path="name"/>
					<font color="red"> <form:errors path="name"></form:errors></font>
					<span class="help-inline"><spring:message code="required"/></span>
					<input size="${errorlist.get(0).length()}" readonly type="text" value="${ errorlist.get(0) }"/>			
				</div>
			</div>
	
			<div class="clearfix">
				<label for="introduced"><spring:message code="introduced"/></label>
				<div class="input">
					<form:input id ="introduced" type="text" name="introduced" path="introduced" />
					<font color="red"> <form:errors path="introduced"></form:errors></font>
					<span class="help-inline"><spring:message code="pattern"/></span>
					<input size="${errorlist.get(1).length()}" readonly type="text" value="${ errorlist.get(1) }"/>				
				</div>
			</div>
			<div class="clearfix">
				<label for="discontinued"><spring:message code="discontinued"/></label>
				<div class="input">
					<form:input id ="discontinued" type="text" name="discontinued" path="discontinued"/>
					<font color="red"> <form:errors path="discontinued"></form:errors></font>
					<span class="help-inline"><spring:message code="pattern"/></span>
					<input size="${errorlist.get(2).length()}" readonly type="text" value="${ errorlist.get(2) }"/>			
				</div>
			</div>
			<div class="clearfix">
				<label for="company"><spring:message code="company"/></label>
				<div class="input">
					<form:select name="company" path="company">
							<option selected value="0">--</option>
						<c:forEach items="${companylist}" var="company">
							<form:option value="${company.id}" path="company">${company.name}</form:option>
							<font color="red"> <form:errors path="company"></form:errors></font>
						</c:forEach>
					</form:select>
					<span class="help-inline"><spring:message code="choose"/></span>
					<input size="${errorlist.get(3).length()}" readonly type="text" value="${ errorlist.get(3) }"/> 
				</div>
			</div>
		</fieldset>
		
		<div class="actions">
			<input type="submit" value="<spring:message code="confirm"/>" class="btn btn-success">
			<spring:message code="or"/> <a href="dashboard" class="btn btn-danger"><spring:message code="cancel"/></a>
		</div>
	</form:form>
</section>

<jsp:include page="../../include/footer.jsp" />