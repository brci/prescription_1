<% 
    ui.decorateWith("appui", "standardEmrPage") 
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.escapeJs(ui.format(patient.familyName)) }, ${ ui.escapeJs(ui.format(patient.givenName)) }" , link: '${ui.pageLink("coreapps", "clinicianfacing/patient", [patientId: patient.id])}'},
        { label: "Prescriptions" }
    ];
</script>

${ ui.includeFragment("coreapps", "patientHeader", [ patient: patient ]) }
${ ui.includeFragment("uicommons", "infoAndErrorMessage")}

<h2>
	Prescriptions
</h2>

<form method="post" id="formprescriptionprint" action="${ ui.pageLink("prescription", "prescriptions", [patientId: patient.id, returnUrl: returnUrl]) }">  
<input type="hidden" name="action" value="print" />

<table id="prescriptions" width="100%" border="1" cellspacing="0" cellpadding="2">
    <thead>
	    <tr>
	        <th>${ ui.message("prescription.medication") }</th>
	        <th>${ ui.message("prescription.dosage") }</th>	      
          <th>${ ui.message("prescription.prescribed") }</th>
          <th>${ ui.message("prescription.actions") }</th>
	        <th>${ ui.message("prescription.print") }</th>	    	                      
	    </tr>
    </thead>

    <tbody>
    	<% if (prescriptions.size() == 0) { %>
            <tr>
                <td colspan="5" align="center" class="allergyStatus">                
                        ${ ui.message("general.none") }
                </td>
            </tr>
        <% } else { %>

         
        <% prescriptions.each { %>
            <tr>

            	<td> ${ ui.format(it.description) }</td>
              <td> ${ ui.format(it.dose) }</td>   
               
					<% if (it.prescriptionFile!=null) { %>             
                              
               	<td> ${ ui.format(it.prescriptionFile) }</td>
               	
					<% } else { %>
					 
					 	<td>&nbsp;</td>
					 	
					<% } %>       


          <% if ( it.prescriptionFile==null || (
          it.prescriptionFile!=null && it.prescriptionFile.size() > 10 && it.prescriptionFile.substring(0,it.prescriptionFile.size()-5).equals(today) 
          ) ) { %>  
          
               <td> 
                    <i class="icon-pencil edit-action" title="${ ui.message("coreapps.edit") }"
                    onclick="location.href='${ ui.pageLink("prescription", "prescription", [prescriptionId:it.id, patientId: patient.id, returnUrl: returnUrl]) }'"></i>                   

                    <i class="icon-remove delete-action" title="${ ui.message("coreapps.delete") }" onclick="location.href='${ ui.pageLink("prescription", "prescriptions", [prescriptionId:it.id, patientId: patient.id, returnUrl: returnUrl, action: "remove"]) }'"></i>  

                </td>
                <td>
                    <input type="checkbox" name="printprescription" value="${it.id}"/>
                </td> 

          <% } else { %>  

            <td>--</td>
            <td>--</td>

          <% } %>              
                                
          </tr>

         

        <% } %>
      <% } %>
    </tbody>
</table>

<br/>

<input type="submit" id="printBt" value="${ ui.message("prescription.print") }" class="confirm right" />

</form>

<button class="cancel" onclick="location.href='${ ui.escapeJs(returnUrl) }'">
    ${ ui.message("uicommons.return") }
</button>

<button class="confirm right" onclick="location.href='${ ui.pageLink("prescription", "prescription", [patientId: patient.id, returnUrl: returnUrl]) }'">
${ ui.message("prescription.addmedication") }
</button>





	