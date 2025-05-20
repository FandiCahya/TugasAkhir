    <!-- Modal for Approval -->
    <div class="modal fade" id="approvalModal" tabindex="-1" aria-labelledby="approvalModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="approvalModalLabel">Approval Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <!-- Form untuk approval -->
                    <form id="approvalForm">
                        @csrf
                        <input type="hidden" id="approval_id">
                        <div class="mb-3">
                            <label for="status" class="form-label">Status</label>
                            <select class="form-select" id="status" name="status">
                                <option value="setuju">Setuju</option>
                                <option value="tidak_setuju">Tidak Setuju</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="catatan" class="form-label">Catatan</label>
                            <textarea class="form-control" id="catatan" name="catatan" rows="3"></textarea>
                        </div>
                        <!-- Add this in the modal body where the signature is shown -->
                        <div class="mb-3" id="signature-preview">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Metode Tanda Tangan</label>
                            <div>
                                <input type="radio" name="signatureMethod" value="upload" checked> Unggah File
                                <input type="radio" name="signatureMethod" value="draw" class="ms-3"> Tanda Tangan
                                Digital
                            </div>
                        </div>
                        <div class="mb-3" id="signature-upload-wrapper">
                            <label for="signature" class="form-label">Unggah File Tanda Tangan</label>
                            <input class="form-control" type="file" id="signature" name="signature">
                        </div>

                        <div class="mb-3 d-none" id="signature-draw-wrapper">
                            <label class="form-label">Tanda Tangan Digital</label>
                            <canvas id="signatureCanvas"
                                style="border: 1px solid #ccc; width: 100%; height: 200px;"></canvas>
                            <button type="button" class="btn btn-sm btn-secondary mt-2" onclick="clearCanvas()">Hapus
                                Tanda Tangan</button>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="approveButton">Approve</button>
                </div>
            </div>
        </div>
    </div>
