Modul 1
Refleksi 1:
Dalam aspek clean code saya rasa saya sudah cukup memenuhinya. Penamaan variabel dan fungsi yang saya tetapkan tidak membingungkan dan jelas. Setiap fungsi yang saya buat, melakukan 1 tugas spesifik seperti halnya fungsi findProduct yang mencari produk berdasarkan idnya
Saya juga Memisahkan logika data di Repository, logika bisnis di Service, dan logika tampilan di Controller sesuai arsitektur Spring Boot.
Selain itu, saya juga menggunakan UUID sebagai ganti ID numerik berurutan mencegah pengguna menebak ID produk lain. Validasi juga dilakukan untuk mencegah pengguna memasukan hal yang tidak sesuai misalnya quantity 0 atau negatif.

Refleksi 2:
Setelah melakukan unit test saya kode saya dapat berjalan sebagaimana mestinya.
Menurut saya jumlah dari unit testnya tidak pasti, yang penting dapat mengcover semua logika yang ada dalam kelasnya. Dengan melihat coverage, saya rasa itu cukup untuk memverifikasi kode saya.
Jika terlihat coveragenya mencapai 100 saya rasa itu belum cukup untuk memastikan kode kita tidak mempunyai bug karena bug bisa saja muncul dari mana saja bahkan tidak kita sangka.

Menurut saya semakin banyak class yang mempunyai struktur kodenya identik maka akan muncul inkonsistensi antara kode-kode yang diuji.
Salah satu mprovement yang dapat dilakukan adalah dengan membuat class induk functional test yang berisi variabel umum seperti base url, meskipun saya belum menerapkannya karena saya perlu mempelajarinya. Dengan usulan tadi, jika terjadi perubahan pada variabel umum kita tinggal mengubahnya di satu file.

Modul 2 Refleksi\
List code quality issue:
1. Unused import
2. Penggunaan field injection seperti @Autowired
3. Tidak adanya penggunaan assertion pada salah satu unit test
4. Empty method

Strategi saya dalam memperbaiki issue yang disebutkan diatas adalah dengan mengikuti instruksi pada sonarqloud terlebih dahulu.
Kemudian jika issue yang di sudah saya coba fix tersebut masih muncul saya akan mencoba mencari tahu di internet. Sejauh ini dalam pengerjaan modul 2 saya hanya mendapatkan issue yang simpel.
Saya juga coba menyelesaikan semua issue yang ada.

Pada CI/CD workflows yang saya buat sudah cukup memenuhi definisi CI/CD itu sendiri. Hal tersebut karena dalam file sudah dikonfigurasi agar otomatis menjalan unit test dan analisis kualitas kode setiap kali push dilakukan.
Jadi setiap ada kode yang baru, akan otomatis dianalisis untuk mendeteksi bug.
Sedangkan dari segi CD, hal ini terpenuhi karena terdapat fitur auto deploy pada koyeb yang membuatnya otomatis memeriksa perubahan yang terjadi di github dan melakukan deployment

Modul 3 Refleksi\ 
Secara keseluruhan saya telah menerapkan semua prinsip SOLID dengan benar:
1. SRP, di prinsip ini saya telah memisahkan Car dan Product controller yang sebelumnya berada pada satu file yang sama. Saya membuat 1 file baru yaitu CarController.java dan memindahkan car controller pada file tersebut, sehingga masing-masing file memegang tanggung jawabnya sendiri (product pada product dan car pada car).
2. OCP dan ISP, disini saya membuat interface seperti service dan repository sehingga jika terdapat object baru contohnya Food, kita hanya perlu mengimplement dari interfacenya saja tanpa perlu mengubah kode lamanya. Project saya ini juga sudah menerapkan ISP karena interface yang saya buat bersifat general sehingga tidak memaksakan suatu object untuk memakai method yang tidak digunakan.
3. LSP, sebenarnya saya secara tidak langsung menerapkan LSP pada Service Implementationnya dimana di masing-masing implementation telah berjalan dengan baik. Sebelumnya interface dari Product dan Car (pada service) telah mengextend interface general yaitu Service, begitu juga dengan yang terjadi pada repository.
4. DIP, saya telah membuat interface contohnya pada service sehingga CarController dan ProductController tidak bergantung pada ServiceImpl, sebaliknya controller tersebut menggunakan interface dari implementationnya.\

Keuntungan yang didapat dari penerapan ini adalah struktur projectnya menjadi lebih rapi dan mudah untuk dikembangkan. Seperti pada penerapan SRP jika controller ditumpuk dalam 1 file maka file tersebut akan menjadi kompleks dan tidak rapi. Kemudian berdasarkan OCP pengembangan projectnya akan menjadi lebih mudah karena kita tidak perlu mengubah kode-kode yang sebelumnya sudah benar\
Kerugian yang didapat dengan tidak menerapkan SOLID adalah kebalikannya dimana struktur project akan menjadi kompleks dan sulit untuk mengembangkannya juga dapat membuat kode yang tidak maintainable