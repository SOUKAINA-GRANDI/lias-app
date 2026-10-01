<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<div id="confirmModal" class="modal-overlay">

    <div class="modal-box">

        <h3 id="modalTitle">Confirmation</h3>

        <p id="modalMessage">
            Êtes-vous sûr ?
        </p>

        <div class="modal-actions">
            <button onclick="closeModal()"
                    class="btn-secondary">
                Annuler
            </button>

            <button id="confirmBtn"
                    class="btn-danger">
                Confirmer
            </button>
        </div>

    </div>

</div>