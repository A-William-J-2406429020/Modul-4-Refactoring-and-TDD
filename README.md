## Modul 4 Bonus 2 Reflection (Refactoring Partner's Code)

**1. Explain what you think about your partner’s code? Are there any aspects that are still lacking from your partner’s code?**
Secara keseluruhan, *code* yang ditulis oleh *partner* saya sudah cukup baik dalam mengimplementasikan fungsionalitas dasar seperti fitur penambahan *Payment* beserta *Service* dan *Repository*-nya. Namun, masih ada beberapa aspek yang kurang dari segi *Clean Code* dan prinsip SOLID. *Code* tersebut masih memiliki *code smells* dalam kategori **Couplers** berupa *tight coupling* akibat ketergantungan pada *concrete class* (melanggar *Dependency Inversion*) dan penggunaan *Field Injection*. Selain itu, terdapat juga kategori **Dispensables** (kode yang tidak berguna) berupa *dead code*, serta potensi **Change Preventers** akibat penggunaan *magic numbers*.

**2. What did you do to contribute to your partner’s code?**
Saya berkontribusi dengan melakukan *Code Review* pada *branch* `order` milik *partner* saya. Saya mengidentifikasi beberapa *code smells* yang secara langsung berdampak negatif pada *maintainability* (kemudahan pemeliharaan) dan *testability* kode. Setelah itu, saya membuat *branch* baru khusus untuk *refactoring* (`refactor/2406423055`), membersihkan *Dispensables*, mengurangi *Couplers*, memastikan semua *Unit Test* berjalan lancar tanpa *NullPointerException*, dan membuat *Pull Request* yang komprehensif kembali ke *branch* `order` beserta penjelasan detail terkait perbaikannya.

**3. What code smells did you find on your partner’s code? & 4. What refactoring steps did you suggest and execute to fix those smells?**

Daftar *code smells* yang saya temukan beserta langkah *refactoring* yang saya eksekusi dapat diamati sebagai berikut:

* **Magic Numbers & Magic Strings (Potensi Change Preventers):**
    * **Smell:** Terdapat *hardcoded value* seperti `"ESHOP"`, `16`, dan `8` di dalam *method* `isValidVoucherCode` pada `PaymentServiceImpl.java`. Hal ini sangat rawan memicu *Shotgun Surgery* (kategori **Change Preventers**) di mana perubahan satu aturan bisnis mengharuskan developer mencari dan mengubah angka-angka ini di berbagai tempat.
    * **Refactoring:** Saya mengekstrak nilai-nilai *hardcoded* tersebut menjadi konstanta `private static final` dengan nama yang representatif (contoh: `VOUCHER_PREFIX`, `VOUCHER_LENGTH`).
* **Redundant / Dead Code (Kategori Dispensables):**
    * **Smell:** Terdapat *method* `getAllPayments()` di `PaymentServiceImpl.java` yang isinya hanya memanggil *method* `getAllPayment()`. Ini adalah beban tak berguna (**Dispensables**) yang membuat *code* menjadi kotor (melanggar prinsip DRY).
    * **Refactoring:** Saya menghapus *method* `getAllPayments()` yang redundan tersebut sepenuhnya dan menyesuaikan pemanggilannya di dalam file *test*.
* **Field Injection Violation (Kategori Couplers):**
    * **Smell:** Dependensi di-injeksi langsung pada *field* menggunakan `@Autowired` di `PaymentServiceImpl.java`. Ini menciptakan *tight coupling* (**Couplers**) yang menyulitkan *Mocking* saat *Unit Testing* karena kita tidak bisa memasukkan dependensi tanpa bantuan *Spring Context*.
    * **Refactoring:** Saya mengubah *field* tersebut menjadi `final` dan mengimplementasikan *Constructor Injection* agar dependensi menjadi eksplisit.
* **Dependency Inversion Principle (SOLID) Violation (Kategori Couplers):**
    * **Smell:** Di dalam `ProductServiceImpl.java`, konstruktor bergantung pada *concrete class* `ProductRepositoryImpl` alih-alih abstraksinya (*Interface*). *Smell* **Couplers** tingkat tinggi ini menyebabkan *Mockito* gagal melakukan *inject mock* saat *testing*, sehingga memicu rentetan `NullPointerException`.
    * **Refactoring:** Saya mengubah tipe parameter pada konstruktor dan deklarasi *field* menjadi *interface* `ProductRepository` untuk mencapai *loose coupling*.
* **Inconsistent Naming Convention:**
    * **Smell:** Nama file *test* tidak sesuai dengan nama *class* di dalamnya (contoh: *file* `ProductServiceTest.java` berisi `class ProductServiceImplTest`). Ini melanggar konvensi penamaan standar di Java dan membingungkan *test runner* serta *developer* lain.
    * **Refactoring:** Saya me-*rename* file-file tersebut (menjadi `ProductServiceImplTest.java` dan `OrderServiceImplTest.java`) agar selaras secara eksak dengan nama *class*-nya.