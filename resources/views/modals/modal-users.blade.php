<div class="modal fade" id="userModal" tabindex="-1" aria-labelledby="userModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="userModalLabel">Add New User</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" ></button>
            </div>
            <div class="modal-body">
                <form id="userForm">
                    @csrf
                    <input type="hidden" id="user-id">
                    <div class="mb-3">
                        <label for="user-name" class="form-label">Name</label>
                        <input type="text" class="form-control" id="user-name" required>
                    </div>
                    <div class="mb-3">
                        <label for="user-email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="user-email" required>
                    </div>
                    <div class="mb-3">
                        <label for="user-role" class="form-label">Role</label>
                        <select class="form-control" id="user-role" required>
                            <option value="user">User</option>
                            <option value="admin">Admin</option>
                            <option value="qmr">QMR</option>
                            <option value="kepalacabang">Kepalacabang</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="user-devisi" class="form-label">Division</label>
                        <select class="form-control" id="user-devisi" required>
                            <option value="egov">E-Gov</option>
                            <option value="opj">OPJ</option>
                            <option value="legal">Legal</option>
                            <option value="c-care">C-Care</option>
                            <option value="nro">NRO</option>
                            <option value="noc">NOC</option>
                            <option value="rumah tangga">Rumah Tangga</option>
                            <option value="helpdesk">Helpdesk</option>
                            <option value="hrd">HRD</option>
                            <option value="retail">Retail</option>
                            <option value="vas">VAS</option>
                            <option value="corp">Corporate</option>
                            <option value="finance">Finance</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="user-password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="user-password">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="submit" class="btn btn-primary" id="submitUserBtn">Save</button>
            </div>
        </div>
    </div>
</div>
