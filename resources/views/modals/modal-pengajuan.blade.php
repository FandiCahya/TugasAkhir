<!-- Modal Tambah/Edit Pengajuan -->
<div class="modal fade" id="pengajuanModal" tabindex="-1" aria-labelledby="pengajuanModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="pengajuanModalLabel">Tambah Pengajuan</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Tutup"></button>
            </div>
            <div class="modal-body">
                <form id="pengajuanForm">
                    @csrf
                    <input type="hidden" id="pengajuan-id">
                    <div class="mb-3">
                        <label for="tgl" class="form-label">Tanggal Pengajuan</label>
                        <input type="date" class="form-control" id="tgl" required>
                    </div>
                    <div class="mb-3">
                        <label for="nama_sistem" class="form-label">Nama Sistem</label>
                        <input type="text" class="form-control" id="nama_sistem" required>
                    </div>
                    <div class="mb-3">
                        <label for="jenis" class="form-label">Jenis Pengajuan</label>
                        <select class="form-control" id="jenis" required>
                            <option value="sistem_baru">Sistem Baru</option>
                            <option value="pengembangan">Pengembangan</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="rencana_anggaran" class="form-label">Rencana Anggaran</label>
                        <select class="form-control" id="rencana_anggaran" required>
                            <option value="termasuk_dalam_perencanaan">Termasuk dalam Perencanaan</option>
                            <option value="tidak_termasuk_perencanaan">Tidak Termasuk dalam Perencanaan</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="masalah" class="form-label">Masalah yang Dihadapi</label>
                        <textarea class="form-control" id="masalah" rows="3" required></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="output" class="form-label">Output yang Diharapkan</label>
                        <textarea class="form-control" id="output" rows="3" required></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="status" class="form-label">Status Pengajuan</label>
                        <select class="form-control" id="status" required>
                            <option value="pending">Pending</option>
                            <option value="accepted">Accepted</option>
                            <option value="rejected">Rejected</option>
                            <option value="developed">Developed</option>
                            <option value="testing">Testing</option>
                            <option value="finished">Finished</option>
                        </select>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                <button type="button" class="btn btn-primary" id="submitPengajuanBtn">Simpan</button>
            </div>
        </div>
    </div>
</div>
