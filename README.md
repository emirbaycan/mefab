# MeFab

[![GitHub stars](https://img.shields.io/github/stars/emirbaycan/mefab.svg?style=flat-square)](https://github.com/emirbaycan/mefab/stargazers)
[![GitHub license](https://img.shields.io/github/license/emirbaycan/mefab.svg?style=flat-square)](https://github.com/emirbaycan/mefab/blob/main/LICENSE)

Modern ve özelleştirilebilir **Floating Action Button (FAB) Overlay** Android kütüphanesi.
Yalnızca **import** edilerek, projelere hızlıca entegre edilebilir.

---

## Özellikler

* Hızlı entegrasyon
* Modern, şık ve dinamik FAB overlay tasarımı
* Sürükle-bırak (drag & drop) desteği
* Minimum bağımlılık, sade kurulum

---

## Kurulum

### 1. Modülü Projene Ekle

Bu kütüphaneyi doğrudan import ederek kullanabilirsin.

**A. Projeye klonla:**

```bash
git clone https://github.com/emirbaycan/mefab.git
```

veya

**B. Git Submodule olarak ekle:**

```bash
git submodule add https://github.com/emirbaycan/mefab.git
```

---

### 2. build.gradle Dosyana Eklemeler

`settings.gradle` veya proje seviyesindeki `build.gradle` dosyanızda modülü ekleyin.

```gradle
include ':mefab'
project(':mefab').projectDir = new File(rootDir, 'mefab')
```

---

### 3. Modülü Uygulama İçine Dahil Et

`app/build.gradle` dosyanıza aşağıdaki satırı ekleyin:

```gradle
implementation project(':mefab')
```

---

## Kullanım

Temel kullanım örneği:

```java
import com.nux.screenrecorder.overlay.Overlay;

// Örneğin bir Activity içinde:
Overlay overlay = new Overlay(this);
overlay.show();
```

Daha fazla detay ve örnek için [docs/usage.md](docs/usage.md) dosyasına göz atabilirsin.
(Dokümantasyon geliştiriliyor)

---

## Geliştirici Notları

* Android min SDK: **21**
* Overlay için gerekli izinleri (SYSTEM\_ALERT\_WINDOW vb.) tanımlamayı unutmayın.
* Kütüphane doğrudan uygulama içinde açılıp kapatılabilir, dışa bağımlılığı yoktur.
* Hataları veya geliştirme taleplerini Issues kısmından bildirebilirsiniz.

---

## Katkıda Bulunanlar

Katkı yapmak için PR gönderebilir veya [Issues](https://github.com/emirbaycan/mefab/issues) üzerinden görüşlerinizi paylaşabilirsiniz.

---

## Lisans

[MIT License](LICENSE)

---
