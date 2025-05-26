<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>Laporan Persetujuan Pengujian</title>
    <style>
        body {
            font-family: 'Times New Roman', Times, serif;
            font-size: 14px;
            line-height: 1.5;
        }

        .line {
            border-top: 2px solid #000;
            margin: 10px 0 20px 0;
        }

        h1,
        h2 {
            text-align: center;
        }

        p {
            margin: 4px 0;
        }

        .section {
            margin: 25px 0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        th,
        td {
            padding: 8px 12px;
            border: 1px solid #aaa;
            text-align: center;
        }

        .signature {
            max-height: 100px;
            margin-bottom: 8px;
        }

        /* Page break for print/PDF */
        .page-break {
            page-break-before: always;
        }

        .section {
            margin-bottom: 12px;
        }

        table.info-table {
            width: 100%;
            border-collapse: collapse;
            table-layout: fixed;
        }

        .info-table td {
            padding: 8px;
            vertical-align: top;
        }

        .info-table .header {
            font-weight: bold;
            font-size: 16px;
            background-color: #f2f2f2;
        }

        .info-table .label {
            width: 30%;
            font-weight: bold;
            text-align: left;
            white-space: nowrap;
        }

        .info-table .value {
            width: 70%;
            text-align: left;
        }
    </style>
</head>

<body>
    {{-- KOP SURAT --}}
    <div style="display: flex; align-items: center; margin-bottom: 10px;">
        {{-- Logo di kiri --}}
        <div style="flex: 0 0 120px; text-align: left;">
            <img src="{{ public_path('assets/images/lifemedia_logo.png') }}" alt="Logo"
                style="width: 100px; height: auto;">
        </div>

        {{-- Teks di kanan logo --}}
        <div style="flex: 1; text-align: center;">
            <h2 style="margin: 0; font-size: 18px;">PT. SaranaInsan MudaSelaras</h2>
            <p style="margin: 2px 0; font-size: 12px;">
                Jl. Parangtritis No.97, Brontokusuman, Kec. Mergangsan, Kota Yogyakarta,<br>
                Daerah Istimewa Yogyakarta 55153
            </p>
            <p style="margin: 2px 0; font-size: 12px;">
                Telp: (+62) 2746055655 | Email: cs@lifemedia.id
            </p>
        </div>
    </div>
    <div class="line"></div>

    <h2>Laporan Permohonan Perangkat Lunak</h2>

    <div class="section">
        <table class="info-table">
            <tr>
                <td colspan="2" class="header">Informasi Pengajuan</td>
            </tr>
            <tr>
                <td class="label">Tanggal</td>
                <td class="value">: {{ $data['pengajuan']['tgl'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Nama Pemohon</td>
                <td class="value">: {{ $data['pengajuan']['user']['name'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Devisi Pemohon</td>
                <td class="value">: {{ $data['pengajuan']['user']['devisi'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Nama Sistem</td>
                <td class="value">: {{ $data['pengajuan']['nama_sistem'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Jenis</td>
                <td class="value">: {{ $data['pengajuan']['jenis'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Rencana Anggaran</td>
                <td class="value">: {{ $data['pengajuan']['rencana_anggaran'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Masalah</td>
                <td class="value">: {{ $data['pengajuan']['masalah'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Output</td>
                <td class="value">: {{ $data['pengajuan']['output'] ?? '-' }}</td>
            </tr>

        </table>
    </div>

    <div class="section">
        <table class="info-table">
            <tr>
                <td colspan="2" class="header">Informasi Pengembangan</td>
            </tr>
            <tr>
                <td class="label">Tanggal Mulai</td>
                <td class="value">: {{ $data['pengembangan']['tanggal_mulai'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Tanggal Selesai</td>
                <td class="value">: {{ $data['pengembangan']['tanggal_selesai'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Tahap</td>
                <td class="value">: {{ $data['pengembangan']['tahap'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Keterangan</td>
                <td class="value">: {{ $data['pengembangan']['keterangan'] ?? '-' }}</td>
            </tr>
        </table>
    </div>

    <div class="section">
        <table class="info-table">
            <tr>
                <td colspan="2" class="header">Informasi Pengujian</td>
            </tr>
            <tr>
                <td class="label">Tanggal</td>
                <td class="value">: {{ $data['pengujian']['tanggal'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Perangkat Lunak</td>
                <td class="value">: {{ $data['pengujian']['perangkat_lunak'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Versi</td>
                <td class="value">: {{ $data['pengujian']['versi'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Tujuan</td>
                <td class="value">: {{ $data['pengujian']['tujuan'] ?? '-' }}</td>
            </tr>
            <tr>
                <td class="label">Metode</td>
                <td class="value">: {{ $data['pengujian']['metode'] ?? '-' }}</td>
            </tr>
        </table>
    </div>
    {{-- HALAMAN BARU --}}
    <div class="page-break"></div>

    <table class="info-table" ,style="width: 100%; border-collapse: collapse; margin-top: 10px;">
        <thead>
            <tr>
                <td colspan="2" class="header">Detail Approval</td>
            </tr>
            <tr>
                @foreach ($data['persetujuan_pengujian_details']->chunk(2)->first() as $index => $detail)
                    <th style="text-align: center; padding: 10px; border-bottom: 1px solid #000;" colspan="1">
                        Persetujuan {{ $loop->iteration }}
                    </th>
                @endforeach
                @if ($data['persetujuan_pengujian_details']->count() % 2 !== 0)
                    <th style="text-align: center; padding: 10px; border-bottom: 1px solid #000;"></th>
                @endif
            </tr>
        </thead>
        <tbody>
            @foreach ($data['persetujuan_pengujian_details']->chunk(2) as $row)
                {{-- Baris Tanda Tangan --}}
                <tr>
                    @foreach ($row as $detail)
                        <td style="text-align: center; padding: 20px;">
                            @if (!empty($detail['signature']))
                                <img src="{{ public_path('storage/' . $detail['signature']) }}" alt="Tanda Tangan"
                                    style="width: 120px; height: auto;">
                            @else
                                <em>Tanda tangan tidak tersedia</em>
                            @endif
                        </td>
                    @endforeach
                    @if ($row->count() < 2)
                        <td></td> {{-- Kolom kosong jika jumlah ganjil --}}
                    @endif
                </tr>

                {{-- Baris Nama --}}
                <tr>
                    @foreach ($row as $detail)
                        <td style="text-align: center; font-weight: bold; padding-bottom: 10px;">
                            {{ $detail['user']['name'] ?? '-' }}
                        </td>
                    @endforeach
                    @if ($row->count() < 2)
                        <td></td>
                    @endif
                </tr>
            @endforeach
        </tbody>
    </table>

</body>

</html>
