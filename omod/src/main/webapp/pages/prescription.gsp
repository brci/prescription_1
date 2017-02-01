<%
    ui.decorateWith("appui", "standardEmrPage")
   
    ui.includeCss("uicommons", "ngDialog/ngDialog.min.css")  

    def isEdit = prescriptionId != null;      
%>
<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.escapeJs(ui.format(patient.familyName)) }, ${ ui.escapeJs(ui.format(patient.givenName)) }" , link: '${ui.pageLink("coreapps", "clinicianfacing/patient", [patientId: patient.id])}'},
        { label: "Prescriptions", link: '${ui.pageLink("prescription", "prescriptions", [patientId: patient.id, returnUrl: returnUrl])}'},
        { label: "add prescription" }
    ];
    
</script>

${ ui.includeFragment("coreapps", "patientHeader", [ patient: patient ]) }

<div>

     <% if (!isEdit) { %>
        <h2 class="inline">${ ui.message("prescription.addprescription") }</h2>    
      <% } else { %>
       <h2 class="inline">${ ui.message("prescription.editprescription") }</h2> 
     <% }%>


    <% if (isEdit) { %>

        <form method="post" id="formremoveprescription" action="${ ui.pageLink("prescription", "prescriptions", [patientId: patient.id, returnUrl: returnUrl, prescriptionId: prescription.id]) }">  
            <input type="submit" class="cancel inline right" value="${ ui.message("coreapps.delete") }"/>
            <input type="hidden" name="prescriptionId" value="${prescription.id}" />
            <input type="hidden" name="action" value="remove" />
        </form>

    <% } %>



	<form method="post" id="formprescription" action="${ ui.pageLink("prescription", "prescription", [patientId: patient.id, returnUrl: returnUrl]) }">    

        <% if (isEdit) { %>
            <input type="hidden" name="prescriptionId" value="${prescription.id}" />
            <input type="hidden" name="action" value="editing" />
        <% } %>

        <div>        
            <label class="heading">${ ui.message("prescription.medication") }</label> 

            <select name="drugId"> 

            <% if (isEdit) { %>                
                
                <option value="${prescription.drugId}" selected="selected">${prescription.description}</option>
                              
            <% } else { %>

                <option value="0" selected="selected">none</option>

            <% } %>

            <% if (drugstoprescribe.size() > 0) { %>  
              
              <% drugstoprescribe.each { %>
                   
    <option value="${ ui.format(it.id) }">${ ui.format(it.drugConcatenated) } </option>
       
              <% } %>
              
            <% } %>
                 
            </select>      

        </div>








        <div>
            <label class="heading">${ ui.message("prescription.dosage") }</label>

             <% if (isEdit) { %>
                <input type="text" maxlength="255" name="dose" value="${prescription.dose}"/>                  
            <% } else { %>           
                <input type="text" maxlength="255" name="dose" />      
             <% } %>  
                    
        </div>  

        <div>
            <label class="heading">${ ui.message("prescription.advice") }</label>

             <% if (isEdit) { %>
                <input type="text" maxlength="255" name="dose" value="${prescription.advice}"/>                  
            <% } else { %>           
                <input type="text" maxlength="255" name="advice" />      
             <% } %>  
      
	    </div> 

	    <div id="actions">
	        <input type="submit" id="addPrescriptionBtn" class="confirm right"  
            value="${ ui.message("coreapps.save") }" />

	        <input type="button" class="cancel" value="${ ui.message("coreapps.cancel") }"
	         onclick="location.href='${ ui.pageLink("prescription", "prescriptions", [patientId: patient.id, returnUrl: returnUrl ]) }'" />
	    </div>
	</form>
</div>
