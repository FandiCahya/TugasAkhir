    <div class="modal fade" id="CatatanPengujianModal" tabindex="-1" aria-labelledby="CatatanPengujianModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="CatatanPengujianModalLabel">Edit Catatan Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="CatatanPengujianForm">
                        @csrf
                        <input type="hidden" id="catatan_pengujian_id">
                        <div class="mb-3">
                            <label for="uraian" class="form-label">Uraian</label>
                            <input type="text" class="form-control" id="uraian" required>
                        </div>
                        <div class="mb-3">
                            <label for="rencana_tindak_lanjut" class="form-label">Rencana Tindak Lanjut</label>
                            <input type="text" class="form-control" id="rencana_tindak_lanjut" required>
                        </div>

                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                    <button type="button" class="btn btn-primary" id="saveCatatanPengujianBtn">Simpan Perubahan</button>
                </div>
            </div>
        </div>
    </div>