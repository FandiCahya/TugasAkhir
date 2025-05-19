    <!-- Edit Modal -->
    <div class="modal fade" id="DetailPengujianModal" tabindex="-1" aria-labelledby="DetailPengujianModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="DetailPengujianModalLabel">Edit Detail Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="DetailPengujianForm">
                        @csrf
                        <input type="hidden" id="detail_pengujian_id">
                        <div class="mb-3">
                            <label for="nama_uji" class="form-label">Nama Uji</label>
                            <input type="text" class="form-control" id="nama_uji" required>
                        </div>
                        <div class="mb-3">
                            <label for="kasus_uji" class="form-label">Kasus Uji</label>
                            <input type="text" class="form-control" id="kasus_uji" required>
                        </div>
                        <div class="mb-3">
                            <label for="hasil_diharapkan" class="form-label">Hasil Diharapkan</label>
                            <input type="text" class="form-control" id="hasil_diharapkan" required>
                        </div>
                        <div class="mb-3">
                            <label for="hasil_pengujian" class="form-label">Hasil Pengujian</label>
                            <input type="text" class="form-control" id="hasil_pengujian" required>
                        </div>
                        <div class="mb-3">
                            <label for="status" class="form-label">Status</label>
                            <select class="form-select" id="status" required>
                                <option value="OK">OK</option>
                                <option value="Tidak">Tidak</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                    <button type="button" class="btn btn-primary" id="saveDetailPengujianBtn">Simpan Perubahan</button>
                </div>
            </div>
        </div>
    </div>