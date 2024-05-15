package org.alwayslearning.graphpeoplesearch.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Resource {
  @JsonProperty("@odata.type")
  private String odataType;
  private String id;
  private String displayName;
  private String givenName;
  private String surname;
  private String companyName;
  private List<EmailAddress> emailAddresses;
  private List<Phone> phones;
  private String jobTitle;
  private String department;
  private String officeLocation;
  private String additionalOfficeLocation;
  private String personType;
  private String userPrincipalName;
  private String imAddress;
}
