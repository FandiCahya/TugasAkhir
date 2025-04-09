<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>Laporan Persetujuan Pengujian</title>
    <style>
        body {
            font-family: sans-serif;
        }

        h1,
        h3 {
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th,
        td {
            padding: 8px 12px;
            border: 1px solid #aaa;
            text-align: left;
        }

        img.signature {
            max-height: 100px;
            margin-bottom: 5px;
        }
    </style>
</head>

<body>
    <h1>Laporan Permohonan Perangkat Lunak</h1>

    <h2>Informasi Pengajuan</h2>
    <p><strong>Tanggal:</strong> {{ $data['pengajuan']['tgl'] ?? '-' }}</p>
    <p><strong>Nama Sistem:</strong> {{ $data['pengajuan']['nama_sistem'] ?? '-' }}</p>
    <p><strong>Jenis:</strong> {{ $data['pengajuan']['jenis'] ?? '-' }}</p>
    <p><strong>Rencana Anggaran:</strong> {{ $data['pengajuan']['rencana_anggaran'] ?? '-' }}</p>
    <p><strong>Masalah:</strong> {{ $data['pengajuan']['masalah'] ?? '-' }}</p>
    <p><strong>Output:</strong> {{ $data['pengajuan']['output'] ?? '-' }}</p>

    <h2>Informasi Pengembangan</h2>
    <p><strong>Tanggal Mulai:</strong> {{ $data['pengembangan']['tanggal_mulai'] ?? '-' }}</p>
    <p><strong>Tanggal Selesai:</strong> {{ $data['pengembangan']['tanggal_selesai'] ?? '-' }}</p>
    <p><strong>Tahap:</strong> {{ $data['pengembangan']['tahap'] ?? '-' }}</p>

    <h2>Informasi Pengujian</h2>
    <p><strong>Tanggal:</strong> {{ $data['pengujian']['tanggal'] ?? '-' }}</p>
    <p><strong>Perangkat Lunak:</strong> {{ $data['pengujian']['perangkat_lunak'] ?? '-' }}</p>
    <p><strong>Versi:</strong> {{ $data['pengujian']['versi'] ?? '-' }}</p>
    <p><strong>Tujuan:</strong> {{ $data['pengujian']['tujuan'] ?? '-' }}</p>
    <p><strong>Metode:</strong> {{ $data['pengujian']['metode'] ?? '-' }}</p>

    <h3>Detail Persetujuan</h3>
    <table style="width: 100%; border-collapse: collapse;" border="1">
        <thead>
            <tr>
                <th colspan="2" style="text-align: center;">Persetujuan</th>
            </tr>
        </thead>
        <tbody>
            @foreach ($data['persetujuan_pengujian_details']->chunk(2) as $row)
                {{-- Baris TTD --}}
                <tr>
                    @foreach ($row as $detail)
                        <td style="text-align: center; vertical-align: top; padding: 20px;">
                            @if (!empty($detail['signature']))
                                <img src="{{ public_path('storage/' . $detail['signature']) }}" alt="Tanda Tangan"
                                    style="max-height: 100px; margin-bottom: 8px;">
                            @else
                                <p style="margin-bottom: 8px;">Tanda tangan tidak tersedia</p>
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
                        <td style="text-align: center; padding: 5px;">
                            <strong>{{ $detail['user']['name'] ?? '-' }}</strong>
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
