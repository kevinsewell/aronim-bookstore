# Generating Certificates

## Generate CA Root Key

```bash
openssl genrsa \
  -des3 \
  -out certificates/aronim-local-root-ca.key \
  4096
```

## Generate CA Root Certificate

```bash
openssl req \
  -days 3650 \
  -key certificates/aronim-local-root-ca.key \
  -out certificates/aronim-local-root-ca.crt \
  -new \
  -nodes \
  -sha256 \
  -subj "/CN=Aronim Local Root CA/O=Aronim/ST=WC/C=ZA" \
  -x509

```

## Generate Certificate Key

```bash
openssl genrsa \
  -out certificates/wildcard.aronim.local.key \
  2048
```

## Generate Certificate Signing Request (CSR)

```bash
openssl req \
  -new \
  -sha256 \
  -key certificates/wildcard.aronim.local.key \
  -subj "/CN=*.aronim.local/O=Aronim/ST=WC/C=ZA" \
  -out certificates/wildcard.aronim.local.csr
```

## Generate Signed Certificate

```bash
openssl x509 \
  -days 365 \
  -extfile certificates/wildcard.aronim.local.ext \
  -in certificates/wildcard.aronim.local.csr \
  -CA certificates/aronim-local-root-ca.crt \
  -CAkey certificates/aronim-local-root-ca.key \
  -CAcreateserial \
  -out certificates/wildcard.aronim.local.crt \
  -req \
  -sha256
```

## Convert to PKCS12
```bash

cat certificates/wildcard.aronim.local.crt > certificates/wildcard.aronim.local.pem
cat certificates/aronim-local-root-ca.crt >> certificates/wildcard.aronim.local.pem

openssl pkcs12 \
  -CAfile certificates/aronim-local-root-ca.crt \
  -caname aronim-local-root-ca \
  -export \
  -in certificates/wildcard.aronim.local.pem \
  -inkey certificates/wildcard.aronim.local.key \
  -name wildcard.aronim.local \
  -out certificates/wildcard.aronim.local.p12

# Password: password
```

## Import PKCS12 Certificate into `server.keystore`

```bash

rm certificates/server.keystore

keytool -importkeystore \
  -destkeystore certificates/server.keystore \
  -srckeystore certificates/wildcard.aronim.local.p12 \
  -srcstoretype PKCS12

# Password: password
```

## Add Certificate to Trust Store for 

```bash
cp $JAVA_HOME/lib/security/cacerts certificates/cacerts

keytool -import \
  -alias aronim-local-root-ca \
  -file certificates/aronim-local-root-ca.crt \
  -storetype JKS \
  -keystore certificates/cacerts \
  -storepass changeit

```

```bash
sudo security add-trusted-cert \
  -k /Library/Keychains/System.keychain \
  -d certificates/aronim-local-root-ca.crt
```
