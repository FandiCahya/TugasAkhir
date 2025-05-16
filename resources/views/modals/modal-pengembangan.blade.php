<!-- Modal Tambah/Edit Pengembangan -->
<div class="modal fade" id="pengembanganModal" tabindex="-1" aria-labelledby="pengembanganModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="pengembanganModalLabel">Tambah/Edit Pengembangan</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Tutup"></button>
            </div>
            <div class="modal-body">
                <form id="pengembanganForm">
                    @csrf
                    
                    <input type="hidden" id="pengembangan-id">
                    <div class="mb-3">
                        <label for="pengajuan_id" class="form-label">Pilih Pengajuan</label>
                        <select id="pengajuan_id" name="pengajuan_id" class="form-select" required>
                            <option value="">-- Pilih Pengajuan --</option>
                            <!-- Opsi pengajuan akan dimasukkan lewat JS -->
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="pengajuan" class="form-label">Nama Sistem</label>
                        <input type="text" class="form-control" id="pengajuan" required readonly>
                    </div>

                    <div class="mb-3">
                        <label for="user" class="form-label">User</label>
                        <input type="text" class="form-control" id="user" required readonly>
                    </div>

                    <div class="mb-3">
                        <label for="tgl_mulai" class="form-label">Tanggal Mulai</label>
                        <input type="date" class="form-control" id="tgl_mulai" required>
                    </div>

                    <div class="mb-3">
                        <label for="tgl_selesai" class="form-label">Tanggal Selesai</label>
                        <input type="date" class="form-control" id="tgl_selesai" required>
                    </div>

                    <div class="mb-3 row">
                        <label class="form-label col-sm-3">Tahap</label>
                        <div class="col-sm-9">
                            <div class="form-check mb-1">
                                <input class="form-check-input tahap-checkbox" type="checkbox" value="Analisis"
                                    id="tahap-analisis">
                                <label class="form-check-label" for="tahap-analisis">Analisis</label>
                            </div>
                            <div class="form-check mb-1">
                                <input class="form-check-input tahap-checkbox" type="checkbox" value="Desain UI UX"
                                    id="tahap-desain">
                                <label class="form-check-label" for="tahap-desain">Desain UI UX</label>
                            </div>
                            <div class="form-check mb-1">
                                <input class="form-check-input tahap-checkbox" type="checkbox" value="Pengerjaan"
                                    id="tahap-pengerjaan">
                                <label class="form-check-label" for="tahap-pengerjaan">Pengerjaan</label>
                            </div>
                            <div class="form-check mb-1">
                                <input class="form-check-input tahap-checkbox" type="checkbox" value="Penyelesaian"
                                    id="tahap-penyelesaian">
                                <label class="form-check-label" for="tahap-penyelesaian">Penyelesaian</label>
                            </div>
                            <div class="form-check mb-1">
                                <input class="form-check-input tahap-checkbox" type="checkbox" value="Testing"
                                    id="tahap-testing">
                                <label class="form-check-label" for="tahap-testing">Testing</label>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="persentase" class="form-label">Persentase</label>
                        <input type="text" class="form-control" id="persentase" readonly>
                    </div>


                    <div class="mb-3">
                        <label for="keterangan" class="form-label">Keterangan</label>
                        <textarea class="form-control" id="keterangan" rows="3" required></textarea>
                    </div>

                    <div class="mb-3">
                        <label for="status_pengembangan" class="form-label">Status Pengembangan</label>
                        <select class="form-control" id="status_pengembangan" required>
                            <option value="developed">Developed</option>
                            <option value="finished">Finished</option>
                        </select>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                <button type="button" class="btn btn-primary" id="submitPengembanganBtn">Simpan</button>
            </div>
        </div>
    </div>
</div>
