/*
 * Copyright 2020 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tech.pegasys.teku.networks;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static tech.pegasys.teku.spec.networks.Eth2Network.LESS_SWIFT;
import static tech.pegasys.teku.spec.networks.Eth2Network.MAINNET;
import static tech.pegasys.teku.spec.networks.Eth2Network.MINIMAL;
import static tech.pegasys.teku.spec.networks.Eth2Network.PYRMONT;
import static tech.pegasys.teku.spec.networks.Eth2Network.SWIFT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import tech.pegasys.teku.infrastructure.unsigned.UInt64;
import tech.pegasys.teku.spec.Spec;
import tech.pegasys.teku.spec.SpecFactory;
import tech.pegasys.teku.spec.SpecVersion;
import tech.pegasys.teku.spec.datastructures.eth1.Eth1Address;
import tech.pegasys.teku.spec.networks.Eth2Network;

public class Eth2NetworkConfiguration {
  private static final int DEFAULT_STARTUP_TARGET_PEER_COUNT = 5;
  private static final int DEFAULT_STARTUP_TIMEOUT_SECONDS = 30;

  private final Spec spec;
  private final String constants;
  private final Optional<String> initialState;
  private final boolean usingCustomInitialState;
  private final int startupTargetPeerCount;
  private final int startupTimeoutSeconds;
  private final List<String> discoveryBootnodes;
  private final Optional<Eth1Address> eth1DepositContractAddress;
  private final Optional<UInt64> eth1DepositContractDeployBlock;
  private final boolean balanceAttackMitigationEnabled;

  private Eth2NetworkConfiguration(
      final Spec spec,
      final String constants,
      final Optional<String> initialState,
      final boolean usingCustomInitialState,
      final int startupTargetPeerCount,
      final int startupTimeoutSeconds,
      final List<String> discoveryBootnodes,
      final Optional<Eth1Address> eth1DepositContractAddress,
      final Optional<UInt64> eth1DepositContractDeployBlock,
      final boolean balanceAttackMitigationEnabled) {
    this.spec = spec;
    this.constants = constants;
    this.initialState = initialState;
    this.usingCustomInitialState = usingCustomInitialState;
    this.startupTargetPeerCount = startupTargetPeerCount;
    this.startupTimeoutSeconds = startupTimeoutSeconds;
    this.discoveryBootnodes = discoveryBootnodes;
    this.eth1DepositContractAddress = eth1DepositContractAddress;
    this.eth1DepositContractDeployBlock = eth1DepositContractDeployBlock;
    this.balanceAttackMitigationEnabled = balanceAttackMitigationEnabled;
  }

  public static Eth2NetworkConfiguration.Builder builder(final String network) {
    return builder().applyNetworkDefaults(network);
  }

  public static Eth2NetworkConfiguration.Builder builder(final Eth2Network network) {
    return builder().applyNetworkDefaults(network);
  }

  public static Eth2NetworkConfiguration.Builder builder() {
    return new Builder();
  }

  public Spec getSpec() {
    return spec;
  }

  /**
   * @deprecated Constants should be accessed via {@link SpecVersion}
   * @return The constants resource name or url
   */
  @Deprecated
  public String getConstants() {
    return constants;
  }

  public Optional<String> getInitialState() {
    return initialState;
  }

  public boolean isUsingCustomInitialState() {
    return usingCustomInitialState;
  }

  public Integer getStartupTargetPeerCount() {
    return startupTargetPeerCount;
  }

  public Integer getStartupTimeoutSeconds() {
    return startupTimeoutSeconds;
  }

  public List<String> getDiscoveryBootnodes() {
    return discoveryBootnodes;
  }

  public Optional<Eth1Address> getEth1DepositContractAddress() {
    return eth1DepositContractAddress;
  }

  public Optional<UInt64> getEth1DepositContractDeployBlock() {
    return eth1DepositContractDeployBlock;
  }

  public boolean isBalanceAttackMitigationEnabled() {
    return balanceAttackMitigationEnabled;
  }

  @Override
  public String toString() {
    return constants;
  }

  public static class Builder {
    private String constants;
    private Optional<String> initialState = Optional.empty();
    private boolean usingCustomInitialState = false;
    private int startupTargetPeerCount = DEFAULT_STARTUP_TARGET_PEER_COUNT;
    private int startupTimeoutSeconds = DEFAULT_STARTUP_TIMEOUT_SECONDS;
    private List<String> discoveryBootnodes = new ArrayList<>();
    private Optional<Eth1Address> eth1DepositContractAddress = Optional.empty();
    private Optional<UInt64> eth1DepositContractDeployBlock = Optional.empty();
    private boolean balanceAttackMitigationEnabled = false;

    public Eth2NetworkConfiguration build() {
      checkNotNull(constants, "Missing constants");

      return new Eth2NetworkConfiguration(
          SpecFactory.create(constants),
          constants,
          initialState,
          usingCustomInitialState,
          startupTargetPeerCount,
          startupTimeoutSeconds,
          discoveryBootnodes,
          eth1DepositContractAddress,
          eth1DepositContractDeployBlock,
          balanceAttackMitigationEnabled);
    }

    public Builder constants(final String constants) {
      this.constants = constants;
      return this;
    }

    public Builder initialState(final String initialState) {
      this.initialState = Optional.of(initialState);
      this.usingCustomInitialState = true;
      return this;
    }

    public Builder initialStateFromClasspath(final String filename) {
      this.initialState =
          Optional.of(Eth2NetworkConfiguration.class.getResource(filename).toExternalForm());
      return this;
    }

    public Builder startupTargetPeerCount(final int startupTargetPeerCount) {
      this.startupTargetPeerCount = startupTargetPeerCount;
      return this;
    }

    public Builder startupTimeoutSeconds(final int startupTimeoutSeconds) {
      this.startupTimeoutSeconds = startupTimeoutSeconds;
      return this;
    }

    public Builder discoveryBootnodes(final String... discoveryBootnodes) {
      this.discoveryBootnodes = asList(discoveryBootnodes);
      return this;
    }

    public Builder eth1DepositContractAddress(final String eth1Address) {
      this.eth1DepositContractAddress = Optional.of(Eth1Address.fromHexString(eth1Address));
      return this;
    }

    public Builder eth1DepositContractAddress(final Optional<Eth1Address> eth1Address) {
      checkNotNull(eth1Address);
      this.eth1DepositContractAddress = eth1Address;
      return this;
    }

    public Builder eth1DepositContractDeployBlock(final long eth1DepositContractDeployBlock) {
      this.eth1DepositContractDeployBlock =
          Optional.of(UInt64.valueOf(eth1DepositContractDeployBlock));
      return this;
    }

    public Builder balanceAttackMitigationEnabled(final boolean balanceAttackMitigationEnabled) {
      this.balanceAttackMitigationEnabled = balanceAttackMitigationEnabled;
      return this;
    }

    public Builder applyNetworkDefaults(final String networkName) {
      Eth2Network.fromStringLenient(networkName)
          .ifPresentOrElse(this::applyNetworkDefaults, () -> reset().constants(networkName));
      return this;
    }

    public Builder applyNetworkDefaults(final Eth2Network network) {
      switch (network) {
        case MAINNET:
          return applyMainnetNetworkDefaults();
        case MINIMAL:
          return applyMinimalNetworkDefaults();
        case PYRMONT:
          return applyPyrmontNetworkDefaults();
        case SWIFT:
          return applySwiftNetworkDefaults();
        case LESS_SWIFT:
          return applyLessSwiftNetworkDefaults();
        default:
          return reset().constants(network.constantsName());
      }
    }

    private Builder reset() {
      constants = null;
      initialState = Optional.empty();
      startupTargetPeerCount = DEFAULT_STARTUP_TARGET_PEER_COUNT;
      startupTimeoutSeconds = DEFAULT_STARTUP_TIMEOUT_SECONDS;
      discoveryBootnodes = new ArrayList<>();
      eth1DepositContractAddress = Optional.empty();
      eth1DepositContractDeployBlock = Optional.empty();

      return this;
    }

    public Builder applyMinimalNetworkDefaults() {
      return reset().constants(MINIMAL.constantsName()).startupTargetPeerCount(0);
    }

    public Builder applySwiftNetworkDefaults() {
      return reset().constants(SWIFT.constantsName()).startupTargetPeerCount(0);
    }

    public Builder applyLessSwiftNetworkDefaults() {
      return reset().constants(LESS_SWIFT.constantsName()).startupTargetPeerCount(0);
    }

    public Builder applyMainnetNetworkDefaults() {
      return reset()
          .constants(MAINNET.constantsName())
          .initialStateFromClasspath("mainnet-genesis.ssz")
          .startupTimeoutSeconds(120)
          .eth1DepositContractAddress("0x00000000219ab540356cBB839Cbe05303d7705Fa")
          .eth1DepositContractDeployBlock(11052984)
          .discoveryBootnodes(
              // PegaSys Teku
              "enr:-KG4QJRlj4pHagfNIm-Fsx9EVjW4rviuZYzle3tyddm2KAWMJBDGAhxfM2g-pDaaiwE8q19uvLSH4jyvWjypLMr3TIcEhGV0aDKQ9aX9QgAAAAD__________4JpZIJ2NIJpcIQDE8KdiXNlY3AyNTZrMaEDhpehBDbZjM_L9ek699Y7vhUJ-eAdMyQW_Fil522Y0fODdGNwgiMog3VkcIIjKA",
              "enr:-KG4QL-eqFoHy0cI31THvtZjpYUu_Jdw_MO7skQRJxY1g5HTN1A0epPCU6vi0gLGUgrzpU-ygeMSS8ewVxDpKfYmxMMGhGV0aDKQtTA_KgAAAAD__________4JpZIJ2NIJpcIQ2_DUbiXNlY3AyNTZrMaED8GJ2vzUqgL6-KD1xalo1CsmY4X1HaDnyl6Y_WayCo9GDdGNwgiMog3VkcIIjKA",

              // Prysmatic Labs
              "enr:-Ku4QImhMc1z8yCiNJ1TyUxdcfNucje3BGwEHzodEZUan8PherEo4sF7pPHPSIB1NNuSg5fZy7qFsjmUKs2ea1Whi0EBh2F0dG5ldHOIAAAAAAAAAACEZXRoMpD1pf1CAAAAAP__________gmlkgnY0gmlwhBLf22SJc2VjcDI1NmsxoQOVphkDqal4QzPMksc5wnpuC3gvSC8AfbFOnZY_On34wIN1ZHCCIyg",
              "enr:-Ku4QP2xDnEtUXIjzJ_DhlCRN9SN99RYQPJL92TMlSv7U5C1YnYLjwOQHgZIUXw6c-BvRg2Yc2QsZxxoS_pPRVe0yK8Bh2F0dG5ldHOIAAAAAAAAAACEZXRoMpD1pf1CAAAAAP__________gmlkgnY0gmlwhBLf22SJc2VjcDI1NmsxoQMeFF5GrS7UZpAH2Ly84aLK-TyvH-dRo0JM1i8yygH50YN1ZHCCJxA",
              "enr:-Ku4QPp9z1W4tAO8Ber_NQierYaOStqhDqQdOPY3bB3jDgkjcbk6YrEnVYIiCBbTxuar3CzS528d2iE7TdJsrL-dEKoBh2F0dG5ldHOIAAAAAAAAAACEZXRoMpD1pf1CAAAAAP__________gmlkgnY0gmlwhBLf22SJc2VjcDI1NmsxoQMw5fqqkw2hHC4F5HZZDPsNmPdB1Gi8JPQK7pRc9XHh-oN1ZHCCKvg",
              // Sigp
              "enr:-Jq4QFs9If3eUC8mHx6-BLVw0jRMbyEgXNn6sl7c77bBmji_afJ-0_X7Q4vttQ8SO8CYReudHsGVvgSybh1y96yyL-oChGV0aDKQtTA_KgAAAAD__________4JpZIJ2NIJpcIQ2_YtGiXNlY3AyNTZrMaECSHaY_36GdNjF8-CLfMSg-8lB0wce5VRZ96HkT9tSkVeDdWRwgiMo",
              "enr:-Jq4QA4kNIdO1FkIHpl5iqEKjJEjCVfp77aFulytCEPvEQOdbTTf6ucNmWSuXjlwvgka86gkpnCTv-V7CfBn4AMBRvIChGV0aDKQtTA_KgAAAAD__________4JpZIJ2NIJpcIQ22Gh-iXNlY3AyNTZrMaEC0EiXxAB2QKZJuXnUwmf-KqbP9ZP7m9gsRxcYvoK9iTCDdWRwgiMo",
              // EF
              "enr:-Ku4QHqVeJ8PPICcWk1vSn_XcSkjOkNiTg6Fmii5j6vUQgvzMc9L1goFnLKgXqBJspJjIsB91LTOleFmyWWrFVATGngBh2F0dG5ldHOIAAAAAAAAAACEZXRoMpC1MD8qAAAAAP__________gmlkgnY0gmlwhAMRHkWJc2VjcDI1NmsxoQKLVXFOhp2uX6jeT0DvvDpPcU8FWMjQdR4wMuORMhpX24N1ZHCCIyg",
              "enr:-Ku4QG-2_Md3sZIAUebGYT6g0SMskIml77l6yR-M_JXc-UdNHCmHQeOiMLbylPejyJsdAPsTHJyjJB2sYGDLe0dn8uYBh2F0dG5ldHOIAAAAAAAAAACEZXRoMpC1MD8qAAAAAP__________gmlkgnY0gmlwhBLY-NyJc2VjcDI1NmsxoQORcM6e19T1T9gi7jxEZjk_sjVLGFscUNqAY9obgZaxbIN1ZHCCIyg",
              "enr:-Ku4QPn5eVhcoF1opaFEvg1b6JNFD2rqVkHQ8HApOKK61OIcIXD127bKWgAtbwI7pnxx6cDyk_nI88TrZKQaGMZj0q0Bh2F0dG5ldHOIAAAAAAAAAACEZXRoMpC1MD8qAAAAAP__________gmlkgnY0gmlwhDayLMaJc2VjcDI1NmsxoQK2sBOLGcUb4AwuYzFuAVCaNHA-dy24UuEKkeFNgCVCsIN1ZHCCIyg",
              "enr:-Ku4QEWzdnVtXc2Q0ZVigfCGggOVB2Vc1ZCPEc6j21NIFLODSJbvNaef1g4PxhPwl_3kax86YPheFUSLXPRs98vvYsoBh2F0dG5ldHOIAAAAAAAAAACEZXRoMpC1MD8qAAAAAP__________gmlkgnY0gmlwhDZBrP2Jc2VjcDI1NmsxoQM6jr8Rb1ktLEsVcKAPa08wCsKUmvoQ8khiOl_SLozf9IN1ZHCCIyg",

              // Nimbus
              "enr:-LK4QA8FfhaAjlb_BXsXxSfiysR7R52Nhi9JBt4F8SPssu8hdE1BXQQEtVDC3qStCW60LSO7hEsVHv5zm8_6Vnjhcn0Bh2F0dG5ldHOIAAAAAAAAAACEZXRoMpC1MD8qAAAAAP__________gmlkgnY0gmlwhAN4aBKJc2VjcDI1NmsxoQJerDhsJ-KxZ8sHySMOCmTO6sHM3iCFQ6VMvLTe948MyYN0Y3CCI4yDdWRwgiOM",
              "enr:-LK4QKWrXTpV9T78hNG6s8AM6IO4XH9kFT91uZtFg1GcsJ6dKovDOr1jtAAFPnS2lvNltkOGA9k29BUN7lFh_sjuc9QBh2F0dG5ldHOIAAAAAAAAAACEZXRoMpC1MD8qAAAAAP__________gmlkgnY0gmlwhANAdd-Jc2VjcDI1NmsxoQLQa6ai7y9PMN5hpLe5HmiJSlYzMuzP7ZhwRiwHvqNXdoN0Y3CCI4yDdWRwgiOM");
    }

    public Builder applyPyrmontNetworkDefaults() {
      return reset()
          .constants(PYRMONT.constantsName())
          .startupTimeoutSeconds(120)
          .eth1DepositContractAddress("0x8c5fecdC472E27Bc447696F431E425D02dd46a8c")
          .eth1DepositContractDeployBlock(3743587)
          .initialStateFromClasspath("pyrmont-genesis.ssz")
          .discoveryBootnodes(
              // @protolambda bootnode 1
              "enr:-Ku4QOA5OGWObY8ep_x35NlGBEj7IuQULTjkgxC_0G1AszqGEA0Wn2RNlyLFx9zGTNB1gdFBA6ZDYxCgIza1uJUUOj4Dh2F0dG5ldHOIAAAAAAAAAACEZXRoMpDVTPWXAAAgCf__________gmlkgnY0gmlwhDQPSjiJc2VjcDI1NmsxoQM6yTQB6XGWYJbI7NZFBjp4Yb9AYKQPBhVrfUclQUobb4N1ZHCCIyg",
              // @protolambda bootnode 2
              "enr:-Ku4QOksdA2tabOGrfOOr6NynThMoio6Ggka2oDPqUuFeWCqcRM2alNb8778O_5bK95p3EFt0cngTUXm2H7o1jkSJ_8Dh2F0dG5ldHOIAAAAAAAAAACEZXRoMpDVTPWXAAAgCf__________gmlkgnY0gmlwhDaa13aJc2VjcDI1NmsxoQKdNQJvnohpf0VO0ZYCAJxGjT0uwJoAHbAiBMujGjK0SoN1ZHCCIyg");
    }
  }
}
