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
               <a href="${ ui.format(<%= request.getContextPath() %>'/moduleServlet/prescription/downloadPrescriptionServlet' + '?filename=' + it) }" target="_blank">${ ui.format(it) }</a>
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