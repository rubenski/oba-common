package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.obaccelerator.common.ObaConstant.*;

@Getter
@Setter
public class SubmittedForm {
    private int stepNr;
    private List<SubmittedValue> values;

    public UUID getSigningCertificate() {
        return getCert(SIGNING_CERTIFICATE_KEY);
    }

    public UUID getTlsCertificate() {
        return getCert(TRANSPORT_CERTIFICATE_KEY);
    }

    public List<UUID> getRedirectUrls() {
        Optional<SubmittedValue> optionalSigningCert = values.stream()
                .filter(v -> v.getKey().equals(REDIRECT_URLS_KEY))
                .findAny();
        if (optionalSigningCert.isPresent()) {
            SubmittedValue submittedValue = optionalSigningCert.get();
            return submittedValue.getValues().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList());
        }
        return null;
    }

    private UUID getCert(String key) {
        Optional<SubmittedValue> optionalSigningCert = values.stream()
                .filter(v -> v.getKey().equals(key))
                .findAny();
        if (optionalSigningCert.isPresent()) {
            SubmittedValue submittedValue = optionalSigningCert.get();
            return UUID.fromString(submittedValue.getValues().get(0));
        }
        return null;
    }

    public void removeMaskedSecrets() {
        List<SubmittedValue> submittedSecrets = values.stream()
                .filter(v -> v.getValues().get(0).equals(SECRET_VALUE))
                .collect(Collectors.toList());
        values.removeAll(submittedSecrets);
    }

    public String getSingularValue(String key) {
        for (SubmittedValue value : values) {
            if (value.getKey().equals(key)) {
                return value.getValues().get(0);
            }
        }
        throw new IllegalArgumentException("Field '" + key + "' not found");
    }

}
