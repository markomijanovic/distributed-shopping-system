# REST endpoint reference

Routes below are relative to the deployed gateway's JAX-RS application base. They are extracted from controller annotations; request success depends on the JMS clients, database state, and the operation's validation rules. Path parameters are part of the original coursework API, and caller-supplied identities must not be treated as production authentication.

| Method | Relative route | Handler |
|---|---|---|
| `POST` | `/artikal/kategorija/{naziv}/{nadkategorija}/{koTrazi}` | `ArtikalController.kreirajKategoriju` |
| `POST` | `/artikal/{naziv}/{opis}/{cena}/{popust}/{nazivKategorije}/{koTrazi}` | `ArtikalController.kreirajArtikal` |
| `POST` | `/artikal/cena/{naziv}/{novaCena}/{koTrazi}` | `ArtikalController.promeniCenu` |
| `POST` | `/artikal/popust/{naziv}/{noviPopust}/{koTrazi}` | `ArtikalController.promeniPopust` |
| `GET` | `/artikal/kategorije` | `ArtikalController.dohvatiSveKategorije` |
| `GET` | `/artikal/prodavac/{korisnickoIme}` | `ArtikalController.dohvatiArtikleProdavca` |
| `GET` | `/artikal/poskupljenje/{kategorija}/{procenat}` | `ArtikalController.BuildujCenu` |
| `GET` | `/artikal/artikal/sigurno_brisanje/{idart}/{koTrazi}` | `ArtikalController.SigurnoBrisanje` |
| `POST` | `/grad/{naziv}/{username}` | `GradController.kreirajGrad` |
| `GET` | `/grad/{koTrazi}` | `GradController.dohvatiSveGradove` |
| `GET` | `/korisnik/provera/{korisnickoIme}/{sifra}` | `KorisnikController.proveriKorisnika` |
| `POST` | `/korisnik/kreiraj/{kIme}/{sifra}/{ime}/{prezime}/{adresa}/{idGrada}/{novac}/{idUloge}/{koSalje}` | `KorisnikController.kreirajKorisnika` |
| `POST` | `/korisnik/novac/{korisnickoIme}/{iznos}/{koTrazi}` | `KorisnikController.dodajNovac` |
| `POST` | `/korisnik/promena/{korisnickoIme}/{novaAdresa}/{noviGrad}/{koTrazi}` | `KorisnikController.promeniPodatke` |
| `GET` | `/korisnik/{koTrazi}` | `KorisnikController.dohvatiSveKorisnike` |
| `POST` | `/korpa/dodaj/{korisnickoIme}/{nazivArtikla}/{kolicina}` | `KorpaController.dodajUKorpu` |
| `DELETE` | `/korpa/ukloni/{korisnickoIme}/{nazivArtikla}/{kolicina}` | `KorpaController.ukloniIzKorpe` |
| `GET` | `/korpa/{korisnickoIme}` | `KorpaController.dohvatiSadrzajKorpe` |
| `POST` | `/lista_zelja/dodaj/{korisnickoIme}/{nazivArtikla}` | `ListaZeljaController.dodajUListuZelja` |
| `DELETE` | `/lista_zelja/ukloni/{korisnickoIme}/{nazivArtikla}` | `ListaZeljaController.ukloniIzListeZelja` |
| `GET` | `/lista_zelja/{korisnickoIme}` | `ListaZeljaController.dohvatiListuZelja` |
| `POST` | `/narudzbina/placanje/{korisnickoIme}/{adresa}/{grad}` | `NarudzbinaController.placanje` |
| `GET` | `/narudzbina/korisnik/{korisnickoIme}` | `NarudzbinaController.dohvatiNarudzbineKorisnika` |
| `GET` | `/narudzbina` | `NarudzbinaController.dohvatiSveNarudzbine` |
| `GET` | `/narudzbina/transakcije` | `NarudzbinaController.dohvatiSveTransakcije` |
