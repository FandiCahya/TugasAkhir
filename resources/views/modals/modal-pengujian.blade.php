<div class="modal fade" id="pengujianModal" tabindex="-1" aria-labelledby="pengujianModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="pengujianModalLabel">Tambah / Edit Pengujian</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Tutup"></button>
            </div>
            <div class="modal-body">
                <form id="pengujianForm">
                    @csrf
                    <input type="hidden" id="pengujian_id">

                    <div class="row">
                        <div class="col-md-9 mb-3">
                            <label for="pengembangan_id" class="form-label">Pilih Pengembangan</label>
                            <!-- Untuk Edit Mode -->
                            <input type="text" id="pengembangan_nama" class="form-control" readonly
                                style="display:none;">
                            <!-- Default: Tambah Mode -->
                            <select id="pengembangan_id" name=pengembangan_id" class="form-select" required>
                                <option value="">-- Pilih Pengembangan --</option>
                            </select>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="perangkat_lunak" class="form-label">Perangkat Lunak</label>
                            <input type="text" class="form-control" id="perangkat_lunak" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="versi" class="form-label">Versi</label>
                            <input type="text" class="form-control" id="versi" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label for="tujuan" class="form-label">Tujuan</label>
                            <input type="text" class="form-control" id="tujuan" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="metode" class="form-label">Metode</label>
                            <input type="text" class="form-control" id="metode" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="tanggal" class="form-label">Tanggal</label>
                            <input type="date" class="form-control" id="tanggal" required>
                        </div>
                        <div class="col-md-12 mb-3" id="pelaksana_select_container">
                            <label for="pelaksana_id" class="form-label">Pelaksana</label>
                            <select class="form-select" id="pelaksana_id" name="pelaksana_id" required>
                                <option value="" disabled selected>Pilih Pelaksana</option>
                            </select>
                        </div>
                        <!-- Input Readonly Pelaksana (tampil saat edit) -->
                        <div class="col-md-12 mb-3" id="pelaksana_readonly_container" style="display:none;">
                            <label for="pelaksana_readonly" class="form-label">Pelaksana</label>
                            <input type="text" id="pelaksana_readonly" class="form-control" readonly>
                            <input type="hidden" id="pelaksana_id_hidden" name="pelaksana_id">
                        </div>

                        {{-- Persetujuan --}}
                        <div id="persetujuan_section" class="col-md-12 mb-3 row">
                            <label class="form-label">Persetujuan Pengguna</label>
                            <div id="user_ids_container">
                                {{-- Checkbox akan di-generate via JavaScript --}}
                            </div>
                        </div>


                        {{-- Detail Pengujian (dinamis bisa ditambah lewat JavaScript) --}}
                        <div id="pengujian_detail_section" class="mb-3">
                            <label class="form-label">Detail Pengujian</label>
                            <div id="pengujian_detail_container">
                                <!-- Dinamis, akan di-generate via JS -->
                            </div>
                            <button type="button" class="btn btn-sm btn-secondary mt-2" id="addDetailBtn">+ Tambah
                                Detail</button>
                        </div>

                        {{-- Catatan Pengujian --}}
                        <div id="catatan_section" class="col-md-12">
                            <hr>
                            <h6>Catatan Pengujian (Opsional)</h6>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="catatan_uraian" class="form-label">Uraian</label>
                            <textarea class="form-control" id="catatan_uraian"></textarea>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="catatan_rencana" class="form-label">Rencana Tindak Lanjut</label>
                            <textarea class="form-control" id="catatan_rencana"></textarea>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="penanggung_jawab_id" class="form-label">Penanggung Jawab</label>
                            <select class="form-select" id="penanggung_jawab_id">
                                <option value="" selected>Pilih Penanggung Jawab</option>
                                {{-- Option via JS --}}
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                <button type="button" class="btn btn-primary" id="savePengujianBtn">Simpan</button>
            </div>
        </div>
    </div>
</div>
