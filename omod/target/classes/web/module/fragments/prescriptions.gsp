<% if (hasPrescriptionViewPrivilege) { %>

<div class="info-section">
    <div class="info-header">
        <i class="icon-file-alt"></i>
        <h3>PRESCRIPTIONS</h3>
      

        <% if (hasPrescriptionModifyPrivilege) { %>
        
          <i class="icon-pencil edit-action right" title="${ ui.message("coreapps.edit") }" onclick="location.href='${ui.pageLink("prescription", "prescriptions", [patientId: patient.patient.id])}';">
          </i>

        <% } %>

    </div>
    <div class="info-body">  
      <% if (prescriptionsgrouped) { %>

        <table>
          <% prescriptionsgrouped.each { %>

              <tr>                
                                             
               <td>
               <a href="${ ui.format('/openmrs-standalone/moduleServlet/prescription/downloadPrescriptionServlet' + '?filename=' + it + '.pdf') }" target="_blank">${ ui.format(it.substring(it.size()-15,it.size())) }</a>
               </td>
                                             
              </tr>
          <% } %>
          
        </table>      
      
      <% } else { %>
        <span> ${ ui.message("general.none") }</span>
      <% } %>
    </div>
</div>

<% } %>